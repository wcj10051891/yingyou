//package xgame.client;
//
//import java.net.InetSocketAddress;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Properties;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
//import org.jboss.netty.bootstrap.ClientBootstrap;
//import org.jboss.netty.buffer.ChannelBuffer;
//import org.jboss.netty.buffer.ChannelBuffers;
//import org.jboss.netty.channel.Channel;
//import org.jboss.netty.channel.ChannelFuture;
//import org.jboss.netty.channel.ChannelHandlerContext;
//import org.jboss.netty.channel.ChannelPipeline;
//import org.jboss.netty.channel.ChannelPipelineFactory;
//import org.jboss.netty.channel.Channels;
//import org.jboss.netty.channel.MessageEvent;
//import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
//import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
//import org.jboss.netty.handler.codec.frame.FrameDecoder;
//import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
//
//import com.baidu.bjf.remoting.protobuf.Codec;
//import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
//import com.baidu.bjf.remoting.protobuf.annotation.Msg;
//import com.shadowgame.rpg.core.AppConfig;
//import com.shadowgame.rpg.core.AppException;
//import com.shadowgame.rpg.msg.ProtoMsg;
//
//public class ProtoBufferClient {
//	public static Map<Integer, Class<?>> id2Msg = new HashMap<Integer, Class<?>>();
//	public static Map<Integer, Codec<Object>> id2Codec = new HashMap<Integer, Codec<Object>>();
//	private static ClientBootstrap bootstarp = null;
//	
//	@SuppressWarnings("unchecked")
//	public static void init() throws Exception {
//		Properties msgRegistry = new Properties();
//		msgRegistry.load(ProtoBufferClient.class.getResourceAsStream("/Messages.txt"));
//		for (Entry<Object, Object> e : msgRegistry.entrySet()) {
//			Integer msgId = Integer.valueOf(e.getKey().toString());
//			Class<?> cls = Class.forName(e.getValue().toString());
//			id2Msg.put(msgId, cls);
//			id2Codec.put(msgId, (Codec<Object>) ProtobufProxy.create(cls));
//		}
//		bootstarp = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
//		bootstarp.setOption("tcpNoDelay", true);
//		bootstarp.setPipelineFactory(new ChannelPipelineFactory() {
//			
//			@Override
//			public ChannelPipeline getPipeline() throws Exception {
//				ChannelPipeline p = Channels.pipeline();
//			     p.addLast("encoder", new OneToOneEncoder() {
//					@Override
//					protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
//						/**
//						 * 4字节长度+protobuf byte
//						 */
//						if((msg.getClass().getAnnotation(Msg.class)) == null)
//							throw new AppException("message must annotated by [" + Msg.class + "]");
//						byte[] data = encodeMsg(msg);
//						int dataBodySize = AppConfig.packet_length_size + data.length;
//						ChannelBuffer packet = ChannelBuffers.buffer(dataBodySize);
//						packet.writeInt(dataBodySize);
//						packet.writeBytes(data);
//						return packet;
//					}
//				});
//			     p.addLast("decoder", new FrameDecoder() {
//					
//					@Override
//					protected Object decode(ChannelHandlerContext ctx, Channel channel,
//							ChannelBuffer buffer) throws Exception {
//						/**
//						 * 4字节长度(长度值=4（len）+数据长度n)+protobuf bytes
//						 */
//						if(buffer.readableBytes() < AppConfig.packet_length_size)
//							return null;
//						
//						buffer.markReaderIndex();
//						int dataBodySize = buffer.readInt();
//						System.out.println("dataBodySize:" + dataBodySize);
//						dataBodySize -= AppConfig.packet_length_size;
//						if(buffer.readableBytes() < dataBodySize) {
//							buffer.resetReaderIndex();
//							return null;
//						}
//						byte[] data = new byte[dataBodySize];
//						buffer.readBytes(data, 0, data.length);
//						try {
//							return decodeMsg(data);
//						} catch (Exception e) {
//							throw new AppException("decode message from player:" + channel.getAttachment() + 
//									", channel:" + channel + " error, data:" + Arrays.toString(data), e);
//						}
//					}
//				});
//			     p.addLast("logic", new SimpleChannelUpstreamHandler() {
//			    	 @Override
//			    	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//			    		 Object attachment = ctx.getChannel().getAttachment();
//			    		 if(attachment != null && attachment instanceof Callback) {
//			    			 ((Callback)attachment).call(e.getMessage());
//			    		 }
//			    	}
//			     });
//			     return p;
//			}
//		});
//	}
//	
//	public static interface Callback {
//		void call(Object msg);
//	}
//	
//	public static byte[] encodeMsg(Object msg) throws Exception {
//		ProtoMsg p = new ProtoMsg();
//		int msgId = msg.getClass().getAnnotation(Msg.class).value();
//		p.id = msgId;
//		p.data = id2Codec.get(msgId).encode(msg);
//		return id2Codec.get(ProtoMsg.class.getAnnotation(Msg.class).value()).encode(p);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static <T> T decodeMsg(byte[] data) throws Exception {
//		ProtoMsg msg = (ProtoMsg)id2Codec.get(ProtoMsg.class.getAnnotation(Msg.class).value()).decode(data);
//		return (T)id2Codec.get(msg.id).decode(msg.data);
//	}
//	
//	
//	public static Channel connect(Callback callback) throws Exception {
//		ChannelFuture f = bootstarp.connect(new InetSocketAddress(9998));
////		ChannelFuture f = bootstarp.connect(new InetSocketAddress("211.147.4.187", 9998));
//		Channel channel = f.awaitUninterruptibly().getChannel();
//		channel.setAttachment(callback);
//		return channel;
//		
////		new Thread(new Runnable() {
////			@Override
////			public void run() {
////				Scanner sc = new Scanner(System.in);
////				while(sc.hasNextLine()) {
////					String cmd = sc.next();
////					if(cmd.equalsIgnoreCase("exit")) {
////						sc.close();
////						System.exit(0);
////					} else if(cmd.equals("send")) {
////						LoginMsg login = new LoginMsg();
////						login.setUsername("12345");
////						login.setPassword("45678");
//////						AlertMsg s = new AlertMsg("尼玛");
////						f.getChannel().write(login);
////					}
////				}
////			}
////		}, "debug exit listener").start();
//	}
//	
//	
//	
//
//	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
//	private static LoginMsg login = new LoginMsg();
//	static {
//		login.username = "用户名";
//		login.password = "密码";
//		login.msgs1 = (Arrays.asList(new NoticeMsg("消息1"), new NoticeMsg("消息2"), new NoticeMsg("消息3")));
//		login.msgs2 = (Arrays.asList(new AlertMsg("消息4"), new AlertMsg("消息5"), new AlertMsg("消息6")));
//	}
//	
//	public static void main(String[] args) {
//		
//	}
//}
