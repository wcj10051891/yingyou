package xgame.core.net.server.http;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.ClosedChannelException;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.UnavailableException;

import org.apache.tomcat.util.http.ServerCookie;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: kofboy@163.com
 * Date: 2010-4-26
 */
@Sharable
public class ServletHandler extends SimpleChannelUpstreamHandler
{
	private static final Logger log = LoggerFactory.getLogger(ServletHandler.class);
    private final NettyHttpServer server;

    public ServletHandler(NettyHttpServer server)
    {
        super();
        this.server = server;
    }

    public NettyHttpServer getServer()
    {
        return server;
    }

    @SuppressWarnings("resource")
	@Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception
    {
        /****
        <servlet-mapping>
            <servlet-name>remotingName</servlet-name>
            <url-pattern>/remoting/*</url-pattern>
        </servlet-mapping>

        servlet.getServletName(): remotingName
        request.getServletPath(): /remoting
        request.getPathInfo(): /user/a/b...
        request.getRequestURI(): /remoting/user/a/b...?key=val#HEAD

        /                          解析路径顺序
        /remoting
        /*
        /remoting/
        /remoting/user
        /remoting/*

        ****/

        HttpRequest request = (HttpRequest) event.getMessage();
        HttpHeaders reqHeaders = request.headers();
        HttpResponse response;
        HttpHeaders respHeaders;
        // Decide whether to close the connection or not.
        boolean close = server.keepAlive > 0 &&
            HttpHeaders.Values.CLOSE.equalsIgnoreCase(reqHeaders.get(HttpHeaders.Names.CONNECTION)) ||
            request.getProtocolVersion().equals(HttpVersion.HTTP_1_0) &&
            !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(reqHeaders.get(HttpHeaders.Names.CONNECTION));

        try
        {
            Servlet servlet = null;
            URI uri;
            String uriStr = request.getUri();
            while (uriStr.startsWith("//"))
                uriStr = uriStr.substring(1);
            try {
                uri = new URI(uriStr);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("URISyntaxException", e);
            }
            String path = uri.getPath();
            String[] pathArray = path.split("/");
            path = "";
            String servletPath = "";
            for (String part : pathArray)
            {
                if ("".equals(part)) continue;
                path += "/";

                servletPath = path;
                servlet = server.getServlet(servletPath);
                if (servlet != null) break;

                servletPath = path + part;
                servlet = server.getServlet(servletPath);
                if (servlet != null) break;

                servlet = server.getServlet(path + "*");
                if (servlet != null) break;
                path += part;
            }
            if (servlet == null)
            {
                /* StaticFile
                if (uri.getPath().startsWith(server.getStaticFileRootAlias()))
                {
                    if (staticFileHandler == null)
                        staticFileHandler = new StaticFileServerHandler(server);
                    staticFileHandler.messageReceived(context, event);
                    return;
                }*/
                throw new UnavailableException("no such servlet: " + path);
            }

            InetSocketAddress remoteAddress = (InetSocketAddress)event.getRemoteAddress();
            NettyHttpServletRequest servletRequest = new NettyHttpServletRequest(context, request, servletPath, remoteAddress);
            NettyHttpServletResponse servletResponse = new NettyHttpServletResponse(context, servletRequest);
            servlet.service(servletRequest, servletResponse);

            // get ChannelBuffer frome response..
            //ChannelBuffer buf = servletResponse.getOutputStream().getChannelBuffer();

            // Build the response object.
            response = servletResponse.getResponse();
            respHeaders = response.headers();
            //response.setContent(buf);
            if (!respHeaders.contains(HttpHeaders.Names.CONTENT_TYPE))
            	respHeaders.set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset="+server.defaultCharset);

            //if (!close)
                // There's no need to add 'Content-Length' header
                // if this is the last response.
                if (!respHeaders.contains(HttpHeaders.Names.CONTENT_LENGTH))
            		respHeaders.set(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(servletResponse.getContentLength()));

            //session
            if (!servletRequest.isRequestedSessionIdValid())      //old session not valid
            {
                NettyHttpSession session = servletRequest.getSession(false);
                if (session != null)                                       //new session was created
                {
                    server.setSession(session.getId(), session);
                    servletResponse.addCookie(new javax.servlet.http.Cookie(
                            NettyHttpSession.SESSION_ID_NAME, session.getId()));
                }
            }

            //cookie
            Set<javax.servlet.http.Cookie> cookies = servletResponse.getCookies();
            if (cookies != null && !cookies.isEmpty())
            {
                for (javax.servlet.http.Cookie cookie : cookies)
                {
                    StringBuffer sb = new StringBuffer();
                    ServerCookie.appendCookieValue(sb,
                            cookie.getVersion(),
                            cookie.getName(),
                            cookie.getValue(),
                            cookie.getPath(),
                            cookie.getDomain(),
                            cookie.getComment(),
                            cookie.getMaxAge(),
                            cookie.getSecure(), false);
                    respHeaders.add(HttpHeaders.Names.SET_COOKIE, sb.toString());
                }
            }

        }
        catch (Exception e)
        {
            HttpResponseStatus status = HttpResponseStatus.INTERNAL_SERVER_ERROR;     //500
            if (e instanceof UnavailableException)
                status = HttpResponseStatus.NOT_FOUND;      //404
            response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
            respHeaders = response.headers();
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            PrintStream out = new PrintStream(new ChannelBufferOutputStream(buf));
            out.println("<h1>" + status.getCode() + " " + status.getReasonPhrase() + "</h1>");
            //e.printStackTrace(out);
            if (e instanceof UnavailableException)
                log.error(e + request.getUri());
            else
                log.error("Servlet throw Exception, requestUrl: " + request.getUri(), e);
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
            response.setContent(buf);
        }
        respHeaders.set("Pragma", "no-cache");
        respHeaders.set("Cache-Control", "no-cache");
        respHeaders.set("Expires", "0");
        //if (server.keepAlive > 0)
        respHeaders.set("Connection", "keep-alive");

        // Write the response.
        event.getChannel().write(response);

        // Close the connection after the write operation is done if necessary.
        if (close)
        {
            //future.addListener(ChannelFutureListener.CLOSE);
            Thread.sleep(1 * 1000);
            event.getChannel().close();
        }
        log.debug("messageReceived return");
     }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        if (e.getCause() instanceof ClosedChannelException ||
           (e.getCause().getCause() != null && e.getCause().getCause() instanceof ClosedChannelException))
        {}
        if (e.getCause() instanceof IOException && e.getCause().getMessage().equals("Connection reset by peer"))
        {}
        else
            log.error("Servlet Exception: ", e.getCause());
        if (e.getChannel().isConnected())
            e.getChannel().close();
    }
}