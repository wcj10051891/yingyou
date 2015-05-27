package xgame.core.net.server.tcp;

import java.util.Arrays;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Service;

public class TcpService implements Service {
	private static final Logger log = LoggerFactory.getLogger(TcpService.class);
	public Channels channels;
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
		this.channels = new Channels();
		this.groups = new Groups(channels);
	}
	
	@Override
	public void stop() throws Exception {
		this.nettyTcpServer.stop();
	}

	public void send(Object message, Channel client) {
		client.write(encoder.encode(message));
		log.info("send to channel{}:{}", client, message);
	}
	
	public void send(Object message, int channelId) {
		channels.getChannel(channelId).write(encoder.encode(message));
		log.debug("send to channel {}:{}", channelId, message);
	}
	
	public void broadcast(Object message, String groupName) {
		groups.broadcast(groupName, encoder.encode(message));
		log.debug("broadcast message:{} to group:{}", message, groupName);
	}
	
	public void broadcast(Object message, String groupName, Integer... excludeChannelIds) {
		groups.broadcast(groupName, encoder.encode(message), excludeChannelIds);
		log.debug("broadcast message:{} to group:{}, excludeChannelIds:{}", message, groupName, Arrays.toString(excludeChannelIds));
	}
	
	public void joinGroup(String groupName, int channelId) {
		groups.join(groupName, channelId);
		log.debug("channel {} join group {}.", channelId, groupName);
	}
	
	public void leaveGroup(String groupName, int channelId) {
		groups.leave(groupName, channelId);
		log.debug("channel {} leave group {}.", channelId, groupName);
	}
	
	public void world(Object message) {
		broadcast(message, Groups.World);
	}
	
	/**
	 * 断掉连接，异步的
	 * @return ChannelFuture
	 */
	public ChannelFuture disconnect(int channelId) {
		log.info("disconnect channel {}.", channelId);
		return channels.getChannel(channelId).close();
	}
}
