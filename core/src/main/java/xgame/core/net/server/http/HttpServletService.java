package xgame.core.net.server.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Service;

/**
 * User: kofboy@163.com
 * Date: 2010-5-4
 */
public class HttpServletService implements Service {
	private static final Logger log = LoggerFactory.getLogger(HttpServletService.class);

    private Map<Integer, NettyHttpServer> serverMap = new HashMap<Integer, NettyHttpServer>(1);

//    private int payPort = Config.HTTP_PAY_PORT > 0 ? Config.HTTP_PAY_PORT : 9001;
    
    private int httpPort;
    private String defaultCharset;
    private int keepAliveSeconds;
    private int sessionActiveTime;
    private Map<String, Servlet> servlets;

    public HttpServletService(int httpPort, String defaultCharset,
			int keepAliveSeconds, int sessionActiveTime, Map<String, Servlet> servlets) {
		super();
		this.httpPort = httpPort;
		this.defaultCharset = defaultCharset;
		this.keepAliveSeconds = keepAliveSeconds;
		this.sessionActiveTime = sessionActiveTime;
		this.servlets = servlets;
	}

	@Override
    public void start() throws Exception
    {
            NettyHttpServer httpServer = new NettyHttpServer(this.httpPort);
            httpServer.setDefaultCharset(this.defaultCharset);
            httpServer.setKeepAlive(this.keepAliveSeconds);
            httpServer.setSessionDefaultActiveTime(this.sessionActiveTime);
//            httpServer.registerServlet("/crossdomain.xml", new Crossdomain());
//            httpServer.registerServlet("/adminremote", new AdminRemoteServlet());
//            httpServer.registerServlet("/login", new Login());
//            httpServer.registerServlet("/gmlogin", new GmLogin());
//            httpServer.registerServlet("/qqapi/login", new QqLogin());
//            httpServer.registerServlet("/pay", new Pay());
//            httpServer.registerServlet("/online", new Online());
//            httpServer.registerServlet("/reg", new RegCount());
//            httpServer.registerServlet("/delay", new Delay());
//            httpServer.registerServlet("/userstat", new Userstat());
//            httpServer.registerServlet("/serverstat", new Serverstat());
//            httpServer.registerServlet("/moneytype", new MoneyType());
//            httpServer.registerServlet("/create", new CreateCharactor());
//            httpServer.registerServlet("/rpc", new PhpRpcDispatcher(AppConfig.PHPRPC_BEAN_PACKAGE));
            for (Entry<String, Servlet> entry : this.servlets.entrySet())
            	httpServer.registerServlet(entry.getKey(), entry.getValue());
            httpServer.start();
            serverMap.put(this.httpPort, httpServer);
//
//        try {
//            NettyHttpServer server = new NettyHttpServer();
//            server.setHttpPort(payPort);
//            server.setDefaultCharset(Config.HTTP_DEFAULT_CHARSET);
//            server.setKeepAlive(Config.HTTP_KEEP_ALIVE ? 3000 : 0);
//            server.setSessionDefaultActiveTime(Config.HTTP_SESSION_ACTIVE_TIME);
//            server.start();
//            serverMap.put(payPort, server);
//        } catch (Exception e) {
//            throw new RuntimeException("http server at " + payPort + " can not start: ", e);
//        }
    }
    
    @Override
    public void stop() throws Exception 
    {
    	for (NettyHttpServer server : serverMap.values()) {
    		server.stop();
    	}
    }

    public NettyHttpServer getHttpServer(int port)
    {
        return serverMap.get(port);
    }

    public Servlet getServlet(String name)
    {
        for (NettyHttpServer server : serverMap.values())
        {
            Servlet servlet = server.getServlet(name);
            if (servlet != null)
                return servlet;
        }
        return null;
    }
}
