package xgame.core.net.remote.servlet;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phprpc.PHPRPC_Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.ClassUtils;
import xgame.core.util.LockFreeDeque;

public class PhpRpcDispatcher extends GenericServlet
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(PhpRpcDispatcher.class);
	private Map<String, RpcServerPool> beanPoolMap = new ConcurrentHashMap<String, RpcServerPool>();
	private String phprpcBeanPackage;
	private static final String PRIVATE_KEY = "test";
	
	public PhpRpcDispatcher(String phprpcBeanPackage) {
		this.phprpcBeanPackage = phprpcBeanPackage;
	}
	
    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException
    {
        String path = ((HttpServletRequest)request).getPathInfo();
        if (path == null || path.equals("/"))
            throw new UnavailableException("no default bean here");
        String beanName = path;
        int index = path.indexOf("/", 1);
        if (index > 0)
            beanName = beanName.substring(0, index);
        beanName = beanName.toLowerCase();

        RpcServerPool pool = beanPoolMap.get(beanName);
        if (pool == null)
            throw new UnavailableException("no such bean: " + beanName);
        PHPRPC_Server server = null;
        try {
            server = pool.getServer();
            server.setCharset(request.getCharacterEncoding());
            String func = request.getParameter("phprpc_func");
            if (func != null && func.trim().equals(""))        //显示方法列表
                request.getParameterValues("phprpc_func")[0] = null;
            server.start((HttpServletRequest)request, (HttpServletResponse)response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        } finally {
            if (server != null)
                pool.putServer(server);
        }
    }

    public void init(ServletConfig servletconfig) throws ServletException
    {
    	for(Class<?> clazz : ClassUtils.scanPackage(this.phprpcBeanPackage)) {
            try
            {
            	String beanName = clazz.getSimpleName().toLowerCase();
                if (clazz.isInterface()
                    || Modifier.isAbstract(clazz.getModifiers())
                    || !Modifier.isPublic(clazz.getModifiers()))
                    continue;

                RpcServerPool pool = new RpcServerPool(clazz);
                pool.putServer(pool.getServer());        //初始化一个实例
                beanPoolMap.put("/" + beanName, pool);
            	log.debug("[remoting/" + beanName + "] " + clazz.getName());
            }
            catch (Exception e)
            {
                throw new ServletException("Servlet init Exception: ", e);
            }
        }
    }

    public void destroy()
    {
        beanPoolMap.clear();
        beanPoolMap = null;
    }

    private class RpcServerPool
    {
        Deque<PHPRPC_Server> deque = new LockFreeDeque<PHPRPC_Server>();
        Class<?> clazz;

        RpcServerPool(Class<?> clazz)        //取出
        {
            this.clazz = clazz;
        }

        PHPRPC_Server getServer() throws InstantiationException, IllegalAccessException
        {
            PHPRPC_Server server = deque.poll();
            if (server == null)
            {
                server = new PHPRPC_Server();
                server.add(clazz.newInstance());
                server.setDebugMode(true);
                server.setPrivateKey(PRIVATE_KEY);
                //server.setCharset("UTF-8");
            }
            return server;
        }

        void putServer(PHPRPC_Server server)        //放回
        {
            deque.push(server);
        }
    }
}