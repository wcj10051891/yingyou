package xgame.core.net.server.tcp;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import xgame.core.net.NetException;

public class NettyTcpServer {
	private ServerBootstrap server;
	private ExecutorService bossPool;
	private ExecutorService workersPool;
	private ChannelUpstreamHandler logicHandler;
	private StatHandler statHandler;
	private int tcpPort;
	private Class<? extends ChannelUpstreamHandler> decoderClass;
	
	public NettyTcpServer(int tcpPort, Class<? extends ChannelUpstreamHandler> decoderClass, ChannelUpstreamHandler logicHandler, boolean isStatistics) {
		this.tcpPort = tcpPort;
		this.decoderClass = decoderClass;
		this.logicHandler = logicHandler;
		if(isStatistics)
			this.statHandler = new StatHandler();
	}

	public void start() {
		if(server == null) {
			bossPool = Executors.newCachedThreadPool();
			workersPool = Executors.newCachedThreadPool();
			server = new ServerBootstrap(new NioServerSocketChannelFactory(
				bossPool, workersPool));
			server.setOption("child.tcpNoDelay", true);
			server.setOption("child.keepAlive", true);
			server.setOption("reuseAddress", true);
			server.setPipelineFactory(new ChannelPipelineFactory() {
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline pipeline = Channels.pipeline();
					try {
						pipeline.addLast("decoder", decoderClass.newInstance());
					} catch (Exception e) {
						throw new NetException("create decoderClass instance failure.", e);
					}
					if(statHandler != null)
						pipeline.addLast("stat", statHandler);
					pipeline.addLast("logic", logicHandler);
					return pipeline;
				}
			});
			server.bind(new InetSocketAddress(tcpPort));
		}
	}
	
	public StatHandler getStatHandler() {
		return this.statHandler;
	}

	public void stop() {
		if(server != null)
			server.releaseExternalResources();
	}
}
