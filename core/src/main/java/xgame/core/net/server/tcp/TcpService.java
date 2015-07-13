package xgame.core.net.server.tcp;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Service;

public class TcpService implements Service {
	private static final Logger log = LoggerFactory.getLogger(TcpService.class);
	public Groups groups;
	
	private int tcpPort;
	private Class<? extends ChannelUpstreamHandler> decoderClass;
	private ChannelUpstreamHandler logicHandler;
	private Encoder encoder;
	private NettyTcpServer nettyTcpServer;
	private boolean isStatistics;
	
	public TcpService(int tcpPort, Class<? extends ChannelUpstreamHandler> decoderClass, 
			Encoder encoder, ChannelUpstreamHandler logicHandler, boolean isStatistics) {
		this.tcpPort = tcpPort;
		this.decoderClass = decoderClass;
		this.encoder = encoder;
		this.logicHandler = logicHandler;
		this.isStatistics = isStatistics;
	}
	
	@Override
	public void start() throws Exception {
		this.nettyTcpServer = new NettyTcpServer(this.tcpPort, this.decoderClass, this.logicHandler, this.isStatistics);
		this.nettyTcpServer.start();
		this.groups = new Groups();
	}
	
	@Override
	public void stop() throws Exception {
		this.nettyTcpServer.stop();
	}
	
	public StatHandler getStatHandler() {
		return this.nettyTcpServer.getStatHandler();
	}

	public void send(Object message, Channel client) {
		client.write(encodeMessage(message));
		log.info("S->C,send to client{}:{}", client, message);
	}
	
	private ChannelBuffer encodeMessage(Object message) {
		if(message instanceof ChannelBuffer)
			return (ChannelBuffer)message;
		else
			return encoder.encode(message);
	}
	
	public void broadcast(Object message, String groupName, Channel... excludeChannels) {
		groups.broadcast(encodeMessage(message), groupName, excludeChannels);
		log.info("S->C,broadcast message:{} to group:{}, exclude:{}", message, groupName, excludeChannels);
	}
	
	public void broadcast(Object message, String groupName, Set<Channel> excludeChannels) {
		groups.broadcast(encodeMessage(message), groupName, excludeChannels);
		log.info("S->C,broadcast message:{} to group:{}, exclude:{}", message, groupName, excludeChannels);
	}
	
	public void broadcast(Object message, Collection<Channel> toChannels, Channel... excludeChannels) {
		Set<Channel> cs = Collections.emptySet();
		if(excludeChannels.length > 0) {
			cs = new HashSet<Channel>(excludeChannels.length);
			for (Channel channel : excludeChannels)
				cs.add(channel);
		}
		this.broadcast(message, toChannels, cs);
	}
	
	public void broadcast(Object message, Collection<Channel> toChannels, Set<Channel> excludeChannels) {
		Object msg = encodeMessage(message);
		if(excludeChannels == null || excludeChannels.isEmpty()) {
			for (Channel channel : toChannels)
				channel.write(msg);
		} else {
			for (Channel channel : toChannels) {
				if (!excludeChannels.contains(channel))
					channel.write(msg);
			}
		}
		log.info("S->C,broadcast message:{} to channels:{}, exclude:{}", message, toChannels, excludeChannels);
	}

	public void joinGroup(String groupName, Channel channel) {
		groups.join(groupName, channel);
		log.info("channel {} join group {}.", channel, groupName);
	}
	
	public void leaveGroup(String groupName, Channel channel) {
		groups.leave(groupName, channel);
		log.info("channel {} leave group {}.", channel, groupName);
	}
	
	/**
	 * 断掉连接，异步的
	 * @return ChannelFuture
	 */
	public ChannelFuture disconnect(Channel channel) {
		log.info("disconnect channel {}.", channel);
		return channel.close();
	}
}
