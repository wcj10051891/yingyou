package xgame.core.net.server.http;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * User: kofboy@163.com
 * Date: 2010-4-26
 */
public class NettyHttpServer
{
	private static final Log log = LogFactory.getLog(NettyHttpServer.class);
    public String defaultCharset;
    public int keepAlive;
    public int sessionDefaultActiveTime;
    private int httpPort;
    private int maxSessionCount = 1000;
    private String staticFileRootAlias = "/";
    private String staticFileRootPath = "/var/www/html";
    private Map<String, NettyHttpSession> sessionMap = new ConcurrentHashMap<String, NettyHttpSession>(0);
    private Map<String, Servlet> servletMap = new ConcurrentHashMap<String, Servlet>(0);
    private ServerBootstrap httpServer;
    
    public NettyHttpServer(int httpPort) {
    	this.httpPort = httpPort;
	}

    public void start() throws ServletException
    {
        for(Servlet servlet : servletMap.values())
            servlet.init(null);

        // Configure the server.
        httpServer = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        httpServer.setPipelineFactory(new ServletPipelineFactory(this));
        // Set up the event pipeline factory.
        //bootstrap.setPipelineFactory(new StaticFileServerPipelineFactory());

        // Bind and start to accept incoming connections.
        httpServer.bind(new InetSocketAddress(httpPort));
        log.info("Http Server start at port " + httpPort + "......");
    }
    
    public void stop()
    {
    	httpServer.releaseExternalResources();
    }

    public NettyHttpSession getSession(String sessionId)
    {
        NettyHttpSession session = sessionMap.get(sessionId);
        if (session != null && !session.isActive())
        {
            session = null;
            sessionMap.remove(sessionId);
        }
        return session;
    }

    public void setSession(String sessionId, NettyHttpSession session)
    {
        sessionMap.put(sessionId, session);
        if (sessionMap.size() > maxSessionCount)
        {
            Iterator<Map.Entry<String, NettyHttpSession>> it = sessionMap.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<String, NettyHttpSession>e = it.next();
                if (! e.getValue().isActive())
                    it.remove();
            }
            if (sessionMap.size() > maxSessionCount * 0.9)
                maxSessionCount += 1000;
        }
    }

    public void registerServlet(String name, Servlet servlet)
    {
        servletMap.put(name, servlet);
    }

    public Servlet getServlet(String name)
    {
        return servletMap.get(name);
    }

    public void setDefaultCharset(String charset)
    {
        this.defaultCharset = charset;
    }

    public void setKeepAlive(int keepAlive)
    {
        this.keepAlive = keepAlive;
    }

    public void setSessionDefaultActiveTime(int time)
    {
        this.sessionDefaultActiveTime = time;
    }

    public void setHttpPort(int port)
    {
        this.httpPort = port;
    }

    public String getStaticFileRootAlias()
    {
        return staticFileRootAlias;
    }

    public String getStaticFileRootPath()
    {
        return staticFileRootPath;
    }

    public void setStaticFileRootPath(String alias, String path)
    {
        this.staticFileRootAlias = alias;
        this.staticFileRootPath = path;
    }

}
