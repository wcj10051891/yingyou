package xgame.core.net.server.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.joda.time.DateTime;

/**
 * User: kofboy@163.com
 * Date: 2010-4-19
 */
public class NettyHttpServletResponse implements HttpServletResponse
{
	private static final String dateHeaderFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private final ChannelHandlerContext context;
    private final DefaultHttpResponse response;
    private final NettyHttpServletRequest request;
    private DynamicChannelBufferBasedServletOutStream outStream;
    private PrintWriter writer;
    private Set<Cookie> cookies;

    public NettyHttpServletResponse(ChannelHandlerContext context, NettyHttpServletRequest request)
    {
        this.context = context;
        this.response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //this.outStream = new DynamicChannelBufferBasedServletOutStream();
        this.request = request;
    }



    DefaultHttpResponse getResponse()
    {
        if (outStream != null)
            response.setContent(outStream.getChannelBuffer());
        return response;
    }

    long getContentLength()
    {
        if (outStream == null)
            return 0;
        return outStream.getChannelBuffer().readableBytes();
    }

    Set<Cookie> getCookies()
    {
        return cookies;
    }

    // method in HttpServletResponse

    public void addCookie(javax.servlet.http.Cookie cookie)
    {
        if (cookies == null)
             cookies = new HashSet<Cookie>();
        cookies.add(cookie);
    }

    public boolean containsHeader(String name)
    {
        return request.getHeader(name) != null;
    }

    /**
     * @deprecated
     */
    public String encodeUrl(String url)
    {
        return encodeURL(url);
    }

    /**
     * @deprecated
     */
    public String encodeRedirectUrl(String url)
    {
        return encodeRedirectURL(url);
    }

    public String encodeURL(String url)
    {
        NettyHttpSession session = request.getSession(false);
        if (url == null || session == null)
            return (url);

        String path = url;
        String query = "";
        String anchor = "";
        int question = url.indexOf('?');
        if (question >= 0) {
            path = url.substring(0, question);
            query = url.substring(question);
        }
        int pound = path.indexOf('#');
        if (pound >= 0) {
            anchor = path.substring(pound);
            path = path.substring(0, pound);
        }
        StringBuffer sb = new StringBuffer(path);
        if( sb.length() > 0 ) { // jsessionid can't be first.
            sb.append(";");
            sb.append(NettyHttpSession.SESSION_ID_NAME);
            sb.append("=");
            sb.append(session.getId());
        }
        sb.append(anchor);
        sb.append(query);
        return (sb.toString());

    }
    
    public String encodeRedirectURL(String url)
    {
        return encodeURL(url);
    }

    public void sendError(int i, String str) throws java.io.IOException
    {
        sendError(i);
    }

    public void sendError(int i) throws IOException
    {
        setStatus(i);
    }

    public void sendRedirect(String url) throws IOException
    {
        resetBuffer();
        response.setStatus(HttpResponseStatus.TEMPORARY_REDIRECT);
        response.headers().set(HttpHeaders.Names.LOCATION, url);
    }

    public void setDateHeader(String s, long l)
    {
        setHeader(s, new DateTime(l).toString(dateHeaderFormat, Locale.US));
    }

    public void addDateHeader(String s, long l) { throw new UnsupportedOperationException("This method is not implemented!"); }

    public void setHeader(String name, String value)
    {
        response.headers().set(name, value);
    }

    public void addHeader(String name, String value)
    {
        response.headers().add(name, value);
    }

    public void setIntHeader(String name, int value)
    {
        setHeader(name, String.valueOf(value));
    }

    public void addIntHeader(String name, int value)
    {
        addHeader(name, String.valueOf(value));    
    }

    public void setStatus(int i)
    {
        response.setStatus(HttpResponseStatus.valueOf(i));
    }

    /**
     * @deprecated
     */
    public void setStatus(int i, String s)
    {
        setStatus(i);
    }




    // method in ServletResponse

    public String getCharacterEncoding()
    {
        String charset = null;
        String contentType = getContentType();
        if (contentType != null)
        {
            int start = contentType.indexOf("charset=");
            if (start >= 0)
            {
                charset = contentType.substring(start + 8);
                int end = charset.indexOf(';');
                if (end >= 0)
                    charset = charset.substring(0, end);
                charset = charset.trim();
                if ((charset.length() > 2) && (charset.startsWith("\"")) && (charset.endsWith("\"")))
                    charset = charset.substring(1, charset.length() - 1);
            }
        }
        if (charset == null)
            charset = ((ServletHandler)context.getHandler()).getServer().defaultCharset;
        return charset;
    }

    public void setCharacterEncoding(String charset)
    {
        String contentType = getContentType();
        if (contentType != null)
        {
            int end = contentType.indexOf(";");
            if (end >= 0)
                contentType = contentType.substring(0, end);
        }
        else
        {
            contentType = "text/html";
        }
        contentType += "; " + "charset=" + charset;
        setContentType(contentType);
    }

    public String getContentType()
    {
        return response.headers().get(HttpHeaders.Names.CONTENT_TYPE);
    }

    public void setContentType(String type)
    {
        setHeader(HttpHeaders.Names.CONTENT_TYPE, type);
    }

    public DynamicChannelBufferBasedServletOutStream getOutputStream()
    {
        if (outStream == null)
            outStream = new DynamicChannelBufferBasedServletOutStream();
        return outStream;
    }

    public PrintWriter getWriter() throws java.io.IOException
    {
        if (writer == null)
            writer = new PrintWriter(
                    new OutputStreamWriter(
                            getOutputStream(), getCharacterEncoding()), true);
        return writer;
    }

    public void setContentLength(int i)
    {
        setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(i));
    }

    public void setBufferSize(int i)
    {}

    public int getBufferSize()
    {
        return Integer.MAX_VALUE;
    }

    public void flushBuffer() throws java.io.IOException
    {
        writer.flush();
    }

    public void resetBuffer()
    {
        if (outStream != null)
            outStream.resetBuffer();
    }

    public boolean isCommitted() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public void reset()
    {
        response.headers().clear();
        resetBuffer();
    }

    public void setLocale(java.util.Locale locale) { throw new UnsupportedOperationException("This method is not implemented!"); }

    public java.util.Locale getLocale() { throw new UnsupportedOperationException("This method is not implemented!"); }




    public class DynamicChannelBufferBasedServletOutStream extends ServletOutputStream
    {
        private ChannelBuffer buf = ChannelBuffers.dynamicBuffer();

        public void write(int b)
        {
            buf.writeByte((byte)b);
        }

        public ChannelBuffer getChannelBuffer()
        {
            return buf;
        }

        public void resetBuffer()
        {
            buf.clear();
        }

        //public void flush()
        //{
            //buf.writeByte(0);
        //}
    }

}
