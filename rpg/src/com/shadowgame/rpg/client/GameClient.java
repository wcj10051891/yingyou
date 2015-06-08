package com.shadowgame.rpg.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.net.msg.Message;

public class GameClient {
	public static Map<Integer, Class<?>> id2Msg = new HashMap<Integer, Class<?>>();
	public static Map<Integer, Codec<Message>> id2Codec = new HashMap<Integer, Codec<Message>>();
	private static ClientBootstrap bootstarp = null;
	
	@SuppressWarnings("unchecked")
	public static void init() throws Exception {
		Properties msgRegistry = new Properties();
		msgRegistry.load(GameClient.class.getResourceAsStream("/Messages.txt"));
		for (Entry<Object, Object> e : msgRegistry.entrySet()) {
			Integer msgId = Integer.valueOf(e.getKey().toString());
			Class<?> cls = Class.forName(e.getValue().toString());
			id2Msg.put(msgId, cls);
			id2Codec.put(msgId, (Codec<Message>) ProtobufProxy.create(cls));
		}
		bootstarp = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstarp.setOption("tcpNoDelay", true);
		bootstarp.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
			     p.addLast("encoder", new OneToOneEncoder() {
					@Override
					protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
						/**
						 * 2字节长度+2字节协议号+protobuf byte
						 */
						if(!(message instanceof Message))
							throw new AppException("message must extends [" + Message.class.getName() + "]");
						Message msg = (Message)message;
						Msg anno = null;
						if((anno = msg.getClass().getAnnotation(Msg.class)) == null)
							throw new AppException("message must annotated by [" + Msg.class + "]");
							
						int msgId = anno.value();
						byte[] data = encodeMsg(msgId, msg);
						int size = 4 + data.length;
						ChannelBuffer packet = ChannelBuffers.buffer(size);
						packet.writeShort(size);
						packet.writeShort(msgId);
						packet.writeBytes(data);
						return packet;
					}
				});
			     p.addLast("decoder", new FrameDecoder() {
					
					@Override
					protected Object decode(ChannelHandlerContext ctx, Channel channel,
							ChannelBuffer buffer) throws Exception {
						/**
						 * 2字节长度+2字节协议号+protobuf bytes
						 */
						if(buffer.readableBytes() < 2)
							return null;
						
						buffer.markReaderIndex();
						short dataBodySize = buffer.readShort();
						dataBodySize -= 2;
						if(buffer.readableBytes() < dataBodySize) {
							buffer.resetReaderIndex();
							return null;
						}
						int msgId = buffer.readShort();
						dataBodySize -= 2;
						byte[] data = new byte[dataBodySize];
						buffer.readBytes(data, 0, data.length);
						try {
							return decodeMsg(msgId, data);
						} catch (Exception e) {
							throw new AppException("protobuf object decode error.", e);
						}
					}
				});
			     p.addLast("logic", new SimpleChannelUpstreamHandler() {
			    	 @Override
			    	public void messageReceived(ChannelHandlerContext ctx,
			    			MessageEvent e) throws Exception {
			    		 Object attachment = ctx.getChannel().getAttachment();
			    		 if(attachment != null && attachment instanceof Callback) {
			    			 ((Callback)attachment).call(e.getMessage());
			    		 }
			    	}
			     });
			     return p;
			}
		});
	}
	
	public static interface Callback {
		void call(Object msg);
	}
	
	public static byte[] encodeMsg(int msgId, Message msg) throws Exception {
		return id2Codec.get(msgId).encode(msg);
	}
	
	public static Message decodeMsg(int msgId, byte[] data) throws Exception {
		return (Message)id2Codec.get(msgId).decode(data);
	}
	
	
	public static Channel connect(Callback callback) throws Exception {
//		ChannelFuture f = bootstarp.connect(new InetSocketAddress(9998));
		ChannelFuture f = bootstarp.connect(new InetSocketAddress("211.147.4.187", 9998));
		Channel channel = f.awaitUninterruptibly().getChannel();
		channel.setAttachment(callback);
		return channel;
		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Scanner sc = new Scanner(System.in);
//				while(sc.hasNextLine()) {
//					String cmd = sc.next();
//					if(cmd.equalsIgnoreCase("exit")) {
//						sc.close();
//						System.exit(0);
//					} else if(cmd.equals("send")) {
//						LoginMsg login = new LoginMsg();
//						login.setUsername("12345");
//						login.setPassword("45678");
////						AlertMsg s = new AlertMsg("尼玛");
//						f.getChannel().write(login);
//					}
//				}
//			}
//		}, "debug exit listener").start();
	}
}
