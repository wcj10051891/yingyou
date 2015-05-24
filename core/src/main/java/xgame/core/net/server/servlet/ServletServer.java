package xgame.core.net.server.servlet;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Servlet;

import org.xnio.Options;

public class ServletServer {
	private int port;
	private Undertow server;

	public ServletServer(int port, String encoding, String root,
			Map<String, String> contextAttribute,
			Map<String, Class<? extends Servlet>> servlets) {
		this.port = port;
		DeploymentInfo deployment = Servlets.deployment()
			.setClassLoader(this.getClass().getClassLoader())
			.setDeploymentName(root)
			.setContextPath("/" + root)
			.setDefaultEncoding(encoding);

		if (contextAttribute != null && !contextAttribute.isEmpty()) {
			for (Entry<String, String> entry : contextAttribute.entrySet())
				deployment.addServletContextAttribute(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Class<? extends Servlet>> entry : servlets.entrySet()) {
			Class<? extends Servlet> servletClass = entry.getValue();
			deployment
			.addServlet(Servlets.servlet(servletClass.getName(), servletClass).addMapping(entry.getKey()).setLoadOnStartup(1));
		}
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(deployment);
		manager.deploy();
		PathHandler httpHandler = null;
		try {
			httpHandler = Handlers.path().addPrefixPath(deployment.getContextPath(), manager.start());
		} catch (Exception e) {
			throw new RuntimeException("servletServer init error", e);
		}
		InetSocketAddress address = new InetSocketAddress(this.port);
		server = Undertow.builder()
				.addHttpListener(address.getPort(), address.getHostName())
				.setSocketOption(Options.TCP_NODELAY, true)
				.setSocketOption(Options.REUSE_ADDRESSES, true)
				.setSocketOption(Options.KEEP_ALIVE, true)
				.setWorkerOption(Options.THREAD_DAEMON, true)
				.setSocketOption(Options.BALANCING_TOKENS, -1)
				.setIoThreads(1)
				.setWorkerThreads(2)
				.setDirectBuffers(false)
				.setHandler(httpHandler)
				.build();
	}

	public void start() {
		server.start();
	}

	public void stop() {
		server.stop();
	}
}
