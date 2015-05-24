package xgame.core.net.server.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.ServerCookie;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * User: kofboy@163.com
 * Date: 2010-4-19
 */
public class NettyHttpServletRequest implements HttpServletRequest
{
    private final String DEFAULT_CHARSET;
    private final ChannelHandlerContext context;
    private final HttpRequest request;
    private final InetSocketAddress remoteAddress;
    private final URI uri;
    private final StringBuffer url;
    private final String servletPath;
    private final String pathInfo;
    //private final QueryStringDecoder query;

    private String charset;
    private ServletInputStream inStream;
    private BufferedReader reader;
    private String requestedSessionId;
    private NettyHttpSession session;
    private javax.servlet.http.Cookie[] cookies;
    private boolean isRequestedSessionIdFromCookie = false;
    private boolean isRequestedSessionIdFromURL = false;
    private Map<String, Object> attributeMap = new HashMap<String, Object>();

    public NettyHttpServletRequest(ChannelHandlerContext context,
                                   HttpRequest request,
                                   String servletPath,
                                   InetSocketAddress remoteAddress)
    {
        this.DEFAULT_CHARSET = ((ServletHandler)context.getHandler()).getServer().defaultCharset;
        this.context = context;
        this.request = request;
        this.remoteAddress = remoteAddress;
        String uriStr = request.getUri();        
        while (uriStr.startsWith("//"))
            uriStr = uriStr.substring(1);

        url = new StringBuffer();
        SocketAddress addr = context.getChannel().getLocalAddress();
        String host = request.headers().get(HttpHeaders.Names.HOST);
        host = host == null ? ((InetSocketAddress)addr).getHostName() : host.split(":")[0];
        int port = ((InetSocketAddress)addr).getPort();
        if (port < 0)
            port = 80; // Work around java.net.URL bug
        String scheme = port == 443 ? "https" : "http";

        url.append(scheme);
        url.append("://");
        url.append(host);
        if ((scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(getRequestURI());

        try
        {
            uri = new URI(url.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Wrong request uri");    
        }
        this.servletPath = servletPath;
        String path = uri.getPath();
        if (! path.startsWith(servletPath))
            throw new IllegalArgumentException("Wrong servletPath: " + servletPath);
        pathInfo = path.substring(servletPath.length());
        //query = new QueryStringDecoder(uri);
        //X-Forwarded-For ：The IP address of the client.
        //X-Forwarded-Host ： The original host requested by the client in the Host HTTP request header.
        //X-Forwarded-Server ：The hostname of the proxy server.
    }

    public NettyHttpServletRequest(String url)
    {
        try
        {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Wrong request uri");
        }
        this.url = new StringBuffer(url);
        servletPath = uri.getPath();
        pathInfo= "";
        DEFAULT_CHARSET = "UTF-8";
        context = null;
        request = null;
        remoteAddress = null;
    }

    private void parseCookies()
    {
        String cookieString = request.headers().get(HttpHeaders.Names.COOKIE);
        if (cookieString == null)
            cookies = new javax.servlet.http.Cookie[0];
        else
        /*{              //netty cookie
            CookieDecoder cookieDecoder = new CookieDecoder();
            Set<Cookie> nettyCookies = cookieDecoder.decode(cookieString);
            if (nettyCookies.size() > 0)
            {
                cookies = new javax.servlet.http.Cookie[nettyCookies.size()];
                int i = 0;
                for(Cookie cookie : nettyCookies)
                {
                    cookies[i++] = new javax.servlet.http.Cookie(cookie.getName(), cookie.getValue());
                    if (NettyHttpSession.SESSION_ID_NAME.equals(cookie.getName()))
                    {
                        isRequestedSessionIdFromCookie = true;
                        requestedSessionId = cookie.getValue();
                    }
                }
            }
        }*/
        {                 //tomcat cookie
            parseCookiesTomcat(cookieString);
            for (javax.servlet.http.Cookie cookie : cookies)
            {
                if (NettyHttpSession.SESSION_ID_NAME.equals(cookie.getName()))
                {
                    isRequestedSessionIdFromCookie = true;
                    requestedSessionId = cookie.getValue();
                    break;
                }
            }
        }

        if (! isRequestedSessionIdFromCookie)
        {
            requestedSessionId = getParameter(NettyHttpSession.SESSION_ID_NAME);
            if (requestedSessionId != null)
                isRequestedSessionIdFromURL = true;
        }

        if (requestedSessionId != null)
        {
            ServletHandler handler = (ServletHandler)context.getHandler();
            session = handler.getServer().getSession(requestedSessionId);
        }
    }



    // method in HttpServletRequest

    public String getAuthType() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public javax.servlet.http.Cookie[] getCookies()
    {
        if (cookies == null) parseCookies();
        return cookies;
    }

    public long getDateHeader(String name) { throw new UnsupportedOperationException("This method is not implemented!"); }

    public String getMethod()
    {
        return request.getMethod().getName();
    }

    public String getPathInfo()
    {
        return pathInfo;
    }

    public String getPathTranslated() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public String getContextPath() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public String getQueryString()
    {
        return uri.getQuery();
    }

    public String getRemoteUser() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public boolean isUserInRole(String s) { throw new UnsupportedOperationException("This method is not implemented!"); }

    public Principal getUserPrincipal() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public String getRequestedSessionId()
    {
        if (cookies == null) parseCookies();
        return requestedSessionId;
    }

    public String getRequestURI()
    {
        return request.getUri();
    }

    public StringBuffer getRequestURL()
    {
        return url;
    }

    public String getServletPath()
    {
        return servletPath;
    }

    public NettyHttpSession getSession(boolean create)
    {
        if (cookies == null) parseCookies();
        if (create && session == null)
            session = new NettyHttpSession(
                    ((ServletHandler)context.getHandler()).getServer().sessionDefaultActiveTime);
        return session;
    }

    public NettyHttpSession getSession()
    {
        return getSession(true);
    }

    public boolean isRequestedSessionIdValid() 
    {
        if (cookies == null) parseCookies();
        return session != null && !session.isNew();
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        if (cookies == null) parseCookies();
        return isRequestedSessionIdFromCookie;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        if (cookies == null) parseCookies();
        return isRequestedSessionIdFromURL;
    }

    /**
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl()
    {
        return isRequestedSessionIdFromURL();
    }



    // method in ServletRequest

    public Object getAttribute(String name)
    {
        return attributeMap.get(name);
    }

    public Enumeration<String> getAttributeNames()
    {
        Vector<String> v = new Vector<String>();
        v.addAll(attributeMap.keySet());
        return v.elements();
    }

    public String getCharacterEncoding()
    {
        if (charset == null)
        {
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
                charset = DEFAULT_CHARSET;
        }
        return charset;
    }

    public void setCharacterEncoding(String s) throws java.io.UnsupportedEncodingException 
    {
        charset = s;
    }

    public int getContentLength()
    {
        try {
            return Integer.parseInt(request.headers().get(HttpHeaders.Names.CONTENT_LENGTH));
        } catch (Exception e) {
            return -1;
        }
    }

    public String getContentType()
    {
        return request.headers().get(HttpHeaders.Names.CONTENT_TYPE);
    }

    public ServletInputStream getInputStream() throws java.io.IOException
    {
        if (inStream == null)
            inStream = new ServletInputStream() {
                public int read() {
                    return request.getContent().readByte();
                }
            };
        return inStream;
    }

    public String getParameter(String name)
    {
        parseParameters();
        return parameters.getParameter(name);
    }

    public Enumeration<String> getParameterNames()
    {
        parseParameters();
        return parameters.getParameterNames();
    }

    public String[] getParameterValues(String name)
    {
        parseParameters();
        return parameters.getParameterValues(name);
    }

    public Map<String, String[]> getParameterMap()
    {
        parseParameters();
        return parameters.getParameterMap();
    }

    public String getProtocol()
    {
        return request.getProtocolVersion().toString();
    }

    public String getScheme()
    {
        return uri.getScheme();
    }

    public String getServerName()
    {
        return uri.getHost();
    }

    public int getServerPort()
    {
        return uri.getPort();
    }

    public BufferedReader getReader() throws java.io.IOException
    {
        if (reader == null)
            reader = new BufferedReader(new InputStreamReader(inStream));
        return reader;
    }

    public String getRemoteAddr() 
    {
        return remoteAddress.getAddress().toString();
    }

    public String getRemoteHost()
    {
        return remoteAddress.getHostName();
    }

    public int getRemotePort()
    {
        return remoteAddress.getPort();
    }

    public void setAttribute(String name, Object obj)
    {
        attributeMap.put(name, obj);
    }

    public void removeAttribute(String name)
    {
        attributeMap.remove(name);
    }

    public Locale getLocale() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public Enumeration<?> getLocales() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public boolean isSecure() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public RequestDispatcher getRequestDispatcher(String s) { throw new UnsupportedOperationException("This method is not implemented!"); }

    /**
     * @deprecated
     */
    public String getRealPath(String s) { throw new UnsupportedOperationException("This method is not implemented!"); }

    public String getLocalName() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public String getLocalAddr() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public int getLocalPort() { throw new UnsupportedOperationException("This method is not implemented!"); }








    //code copy from tomcat (org.apache.catalina.connector.RequestDto.java)


    /**
     * Parse request parameters.
     */
    private Parameters parameters;
    protected void parseParameters()
    {
        if (parameters != null) return;
        parameters = new Parameters();
        parameters.setQuery(uri.getRawQuery());
        //parameters.setQuery(queryMB);
        //parameters.setURLDecoder(new UDecoder());
        //parameters.setHeaders(headers);

        // getCharacterEncoding() may have been overridden to search for
        // hidden form field containing request encoding
        String enc = getCharacterEncoding();

        if (enc != null) {
            parameters.setEncoding(enc);
            parameters.setQueryStringEncoding(enc);
        } else {
            parameters.setEncoding(DEFAULT_CHARSET);
            parameters.setQueryStringEncoding(DEFAULT_CHARSET);
        }

        parameters.handleQueryParameters();

        if (!getMethod().equalsIgnoreCase("POST"))
            return;

        String contentType = getContentType();
        if (contentType == null)
            contentType = "";
        int semicolon = contentType.indexOf(';');
        if (semicolon >= 0) {
            contentType = contentType.substring(0, semicolon).trim();
        } else {
            contentType = contentType.trim();
        }
        if (!("application/x-www-form-urlencoded".equals(contentType)))
            return;

        int len = getContentLength();

        if (len > 0) {
            /*
            int maxPostSize = connector.getMaxPostSize();
            if ((maxPostSize > 0) && (len > maxPostSize)) {
                if (context.getLogger().isDebugEnabled()) {
                    context.getLogger().debug("Post too large");
                }
                return;
            }*/
            if (len > 64000)
                System.out.println("request ContentLength > 64k requestUrl:" + this.getRequestURL());
            byte[] formData = new byte[len];
            /*
            if (len < CACHED_POST_LEN) {
                if (postData == null)
                    postData = new byte[CACHED_POST_LEN];
                formData = postData;
            } else {
                formData = new byte[len];
            }*/
            try {
                if (readPostBody(formData, len) != len) {
                    return;
                }
            } catch (IOException e) {
                // Client disconnect
                return;
            }
            parameters.processParameters(formData, 0, len);
        }

    }


    /**
     * Read post body in an array.
     */
    protected int readPostBody(byte body[], int len)
        throws IOException {

        int offset = 0;
        do {
            int inputLen = getInputStream().read(body, offset, len - offset);
            if (inputLen <= 0) {
                return offset;
            }
            offset += inputLen;
        } while ((len - offset) > 0);
        return len;

    }


    protected void parseCookiesTomcat(String cookieString) {

        if (cookies != null) return;

        Cookies serverCookies = new Cookies();
        serverCookies.processCookieHeader(cookieString);
        int count = serverCookies.getCookieCount();
        if (count <= 0)
            return;

        cookies = new javax.servlet.http.Cookie[count];

        int idx=0;
        for (int i = 0; i < count; i++) {
            ServerCookie scookie = serverCookies.getCookie(i);
            try {
                /*
                we must unescape the '\\' escape character
                */
                javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(scookie.getName().toString(),null);
                int version = scookie.getVersion();
                cookie.setVersion(version);
                cookie.setValue(unescape(scookie.getValue().toString()));
                cookie.setPath(unescape(scookie.getPath().toString()));
                String domain = scookie.getDomain().toString();
                if (domain!=null) cookie.setDomain(unescape(domain));//avoid NPE
                String comment = scookie.getComment().toString();
                cookie.setComment(version==1?unescape(comment):null);
                cookies[idx++] = cookie;
            } catch(IllegalArgumentException e) {
                // Ignore bad cookie
            }
        }
        if( idx < count ) {
            javax.servlet.http.Cookie[] ncookies = new javax.servlet.http.Cookie[idx];
            System.arraycopy(cookies, 0, ncookies, 0, idx);
            cookies = ncookies;
        }

    }

    protected String unescape(String s) {
        if (s==null) return null;
        if (s.indexOf('\\') == -1) return s;
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c!='\\') buf.append(c);
            else {
                if (++i >= s.length()) throw new IllegalArgumentException();//invalid escape, hence invalid cookie
                c = s.charAt(i);
                buf.append(c);
            }
        }
        return buf.toString();
    }

	@Override
	public String getHeader(String name) {
        return request.headers().get(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		Vector<String> v = new Vector<String>();
        v.addAll(request.headers().getAll(name));
        return v.elements();
	}

	@Override
	public Enumeration<String> getHeaderNames() {
        Vector<String> v = new Vector<String>();
        v.addAll(request.headers().names());
        return v.elements();
	}

	@Override
	public int getIntHeader(String name) {
        return Integer.parseInt(getHeader(name));
	}

}
