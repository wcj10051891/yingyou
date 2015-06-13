package xgame.client;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.ToStringBuilder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.msg.AlertMsg;
import com.shadowgame.rpg.msg.LoginMsg;
import com.shadowgame.rpg.msg.LogoutMsg;
import com.shadowgame.rpg.msg.NoticeMsg;
import com.shadowgame.rpg.net.msg.Message;

public class BinaryGameClient {
	public static BinaryMsgService msgService = new BinaryMsgService();
	private static ClientBootstrap bootstarp = null;
	private static final Logger log = LoggerFactory.getLogger(BinaryGameClient.class);
	
	public static void init() throws Exception {
		msgService.start();
		bootstarp = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstarp.setOption("tcpNoDelay", true);
		bootstarp.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline p = Channels.pipeline();
			     p.addLast("encoder", new OneToOneEncoder() {
					@Override
					protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
						if(!(msg instanceof Message))
							throw new AppException("message must instanceof " + Message.class.getName());
						/**
						 * 4字节长度			+		bytes		<br/>
						 * 长度=4(len)+4(协议号)+bytes数据长度
						 */
						ChannelBuffer data = null;
						try {
							data = msgService.encode((Message)msg);
						} catch (Exception e) {
							throw new AppException("encode message error, msg:" + msg, e);
						}
						int dataBodySize = AppConfig.packet_length_size + AppConfig.msgId_size + data.readableBytes();
						ChannelBuffer packet = ChannelBuffers.buffer(dataBodySize);
						packet.writeInt(dataBodySize);
						packet.writeInt(msgService.msg2Id.get(msg.getClass()));
						packet.writeBytes(data);
						return packet;
					}
				});
			     p.addLast("decoder", new FrameDecoder() {
					
					@Override
					protected Object decode(ChannelHandlerContext ctx, Channel channel,
							ChannelBuffer buffer) throws Exception {
						/**
						 * 4字节长度			+		bytes		<br/>
						 * 长度=4(len)+4(协议号)+bytes数据长度
						 */
						if(buffer.readableBytes() < AppConfig.packet_length_size)
							return null;
						
						buffer.markReaderIndex();
						int dataBodySize = buffer.readInt();
						dataBodySize -= AppConfig.packet_length_size;
						if(buffer.readableBytes() < dataBodySize) {
							buffer.resetReaderIndex();
							return null;
						}
						int msgId = buffer.readInt();
						dataBodySize -= AppConfig.msgId_size;
						ChannelBuffer data = ChannelBuffers.buffer(dataBodySize);
						buffer.readBytes(data, dataBodySize);
						try {
							Object msg = msgService.decode(msgId, data);
							return msg;
						} catch (Exception e) {
							throw new AppException("decode message from player:" + channel.getAttachment() + 
									", channel:" + channel + " error", e);
						}
					}
				});
			     p.addLast("logic", new SimpleChannelUpstreamHandler() {
			    	 @Override
			    	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
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
	
	public static Channel connect(Callback callback) throws Exception {
		ChannelFuture f = bootstarp.connect(new InetSocketAddress(9998));
//		ChannelFuture f = bootstarp.connect(new InetSocketAddress("211.147.4.187", 9998));
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
	
	public static void main(String[] args) throws Exception {
		final LoginMsg login = new LoginMsg();
		login.username = "u1";
		login.password = "密码";
		login.msgs1 = (Arrays.asList(new NoticeMsg("消息1"), new NoticeMsg("消息2"), new NoticeMsg("消息3")));
		login.msgs2 = (Arrays.asList(new AlertMsg("消息4"), new AlertMsg("消息5"), new AlertMsg("消息6")));
		
		final LogoutMsg logout = new LogoutMsg();
		
		BinaryGameClient.init();
		final Map<Integer, Channel> channels = new HashMap<Integer, Channel>();
		for (int i = 0; i < 1; i++) {
			Channel channel = connect(login);
			channels.put(i, channel);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner sc = new Scanner(System.in);
				while(sc.hasNextLine()) {
					String cmd = sc.next();
					if(cmd.equalsIgnoreCase("exit")) {
						sc.close();
						System.exit(0);
					} else if(cmd.equalsIgnoreCase("0")) {
//						pool.scheduleAtFixedRate(new Runnable() {
//							@Override
//							public void run() {
								for (Channel c : channels.values()) {
									c.write(login);
								}
//							}
//						}, 1, 1, TimeUnit.SECONDS);
					} else if(cmd.equalsIgnoreCase("1")) {
						channels.get(0).write(logout);
						channels.remove(0);
						if(channels.isEmpty()) {
							sc.close();
							System.exit(0);
						}
					}
				}
			}
		}, "debug exit listener").start();
		
	}
	
	private static Random r = new Random();
	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	private static Channel connect(Object msg) throws Exception {
		return BinaryGameClient.connect(new Callback() {
			@Override
			public void call(Object msg) {
				log.info("received msg:{}", ToStringBuilder.reflectionToString(msg));
			}
		});
	}
	
	private static void sendTest(final Channel client, final Object msg, boolean schedule) {
		if(schedule) {
			pool.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					client.write(msg);
				}
			}, 0, r.nextInt(1000) + 500, TimeUnit.MILLISECONDS);
		} else {
			client.write(msg);
		}
	}
	
	
}
