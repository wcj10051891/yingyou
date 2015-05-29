package xgame.core.net.server.tcp;

import java.util.Arrays;
import java.util.Collection;
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
	
	public TcpService(int tcpPort, Class<? extends ChannelUpstreamHandler> decoderClass, 
			ChannelUpstreamHandler logicHandler, Encoder encoder) {
		this.tcpPort = tcpPort;
		this.decoderClass = decoderClass;
		this.logicHandler = logicHandler;
		this.encoder = encoder;
	}
	
	@Override
	public void start() throws Exception {
		this.nettyTcpServer = new NettyTcpServer(this.tcpPort, this.decoderClass, this.logicHandler);
		this.nettyTcpServer.start();
		this.groups = new Groups();
	}
	
	@Override
	public void stop() throws Exception {
		this.nettyTcpServer.stop();
	}

	public void send(Object message, Channel client) {
		client.write(encoder.encode(message));
		log.info("send to channel{}:{}", client, message);
	}
	
	public void send(ChannelBuffer message, Channel client) {
		client.write(message);
		log.info("send to channel{}:{}", client, message);
	}
	
	public void broadcast(Object message, String groupName, Channel... excludeChannels) {
		this.broadcast(encoder.encode(message), groupName, excludeChannels);
	}
	
	public void broadcast(Object message, String groupName, Set<Channel> excludeChannels) {
		this.broadcast(encoder.encode(message), groupName, excludeChannels);
	}

	public void broadcast(ChannelBuffer message, String groupName, Channel... excludeChannels) {
		groups.broadcast(message, groupName, excludeChannels);
		log.info("broadcast message:{} to group:{}, exclude:{}", message, groupName, excludeChannels);
	}
	
	public void broadcast(ChannelBuffer message, String groupName, Set<Channel> excludeChannels) {
		groups.broadcast(message, groupName, excludeChannels);
		log.info("broadcast message:{} to group:{}, exclude:{}", message, groupName, excludeChannels);
	}
	
	public void broadcast(Object message, Collection<Channel> toChannels, Channel... excludeChannels) {
		this.broadcast(encoder.encode(message), toChannels, excludeChannels);
	}
	
	public void broadcast(Object message, Collection<Channel> toChannels, Set<Channel> excludeChannels) {
		this.broadcast(encoder.encode(message), toChannels, excludeChannels);
	}

	public void broadcast(ChannelBuffer message, Collection<Channel> toChannels, Channel... excludeChannels) {
		this.broadcast(message, toChannels, new HashSet<>(Arrays.asList(excludeChannels)));
	}
	
	public void broadcast(ChannelBuffer message, Collection<Channel> toChannels, Set<Channel> excludeChannels) {
		if(excludeChannels == null || excludeChannels.isEmpty()) {
			for (Channel channel : toChannels)
				channel.write(message);
		} else {
			for (Channel channel : toChannels) {
				if (!excludeChannels.contains(channel))
					channel.write(message);
			}
		}
		log.info("broadcast message:{} to channels:{}, exclude:{}", message, toChannels, excludeChannels);
	}
	
	public void joinGroup(String groupName, Channel channel) {
		groups.join(groupName, channel);
		log.info("channel {} join group {}.", channel, groupName);
	}
	
	public void leaveGroup(String groupName, Channel channel) {
		groups.leave(groupName, channel);
		log.info("channel {} leave group {}.", channel, groupName);
	}
	
	public void world(Object message) {
		broadcast(message, Groups.World);
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
