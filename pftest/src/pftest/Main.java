package pftest;

import java.net.InetSocketAddress;
import java.util.Scanner;
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

import com.google.protobuf.Message;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.protobuf.dto.ResponseDtoPROTO;
import com.shadowgame.rpg.modules.protobuf.dto.ResponseDtoPROTO.ResponseDto;
import com.shadowgame.rpg.modules.protobuf.dto.chat.ChatDtoPROTO.ChatDto;
import com.shadowgame.rpg.modules.protobuf.dto.chat.ChatListDtoPROTO.ChatListDto;


public class Main {
	public static void main(String[] args) {
		ClientBootstrap client = new ClientBootstrap();
		client.setFactory(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		client.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				 ChannelPipeline p = Channels.pipeline();
			     p.addLast("encoder", new OneToOneEncoder() {
					@Override
					protected Object encode(ChannelHandlerContext ctx,
							Channel channel, Object msg) throws Exception {
						byte[] data = ((Message)msg).toByteArray();
						ChannelBuffer packet = ChannelBuffers.buffer(4 + data.length);
						packet.writeInt(data.length);
						packet.writeBytes(data);
						return packet;
					}
				});
			     p.addLast("decoder", new FrameDecoder() {
			    		@Override
			    		protected Object decode(ChannelHandlerContext ctx, Channel channel,
			    				ChannelBuffer buffer) throws Exception {
			    			if(buffer.readableBytes() < 4)
			    				return null;
			    			
			    			buffer.markReaderIndex();
			    			int len = buffer.readInt();
			    			if(buffer.readableBytes() < len) {
			    				buffer.resetReaderIndex();
			    				return null;
			    			}
			    			byte[] data = new byte[len];
			    			buffer.readBytes(data, 0, len);
			    			try {
			    				ResponseDto responseDto = ResponseDtoPROTO.ResponseDto.newBuilder().mergeFrom(data).build();
			    				ChatListDto r = ChatListDto.newBuilder().mergeFrom(responseDto.getResult().getData()).build();
			    				for (ChatDto str : r.getChatsList()) {
									System.out.println("sayerId:" + str.getSayerId() + ", sayerNickname:" + str.getSayerNickname() + ",content:" + str.getContent());
								}
			    				return responseDto;
			    			} catch (Exception e) {
			    				throw new AppException("protobuf object decode error.", e);
			    			}
			    		}
			    	});
			     p.addLast("logic",   new SimpleChannelUpstreamHandler(){
			    	 @Override
			    	public void messageReceived(ChannelHandlerContext ctx,
			    			MessageEvent e) throws Exception {
			    		System.out.println("received");
			    	}
			     });
			     return p;

			}
		});
		final ChannelFuture f = client.connect(new InetSocketAddress(9998));
		f.awaitUninterruptibly();
//		f.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				if(future.isSuccess()) {
//					future.getChannel().s
//				}
//			}
//		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner sc = new Scanner(System.in);
				while(sc.hasNextLine()) {
					String cmd = sc.next();
					if(cmd.equalsIgnoreCase("exit")) {
						sc.close();
						System.exit(0);
					} else if(cmd.equalsIgnoreCase("send")) {
//						RequestDto req = new RequestDto();
//						req.sn = 1;
//						req.method = "m";
//						req.params = "[1,2,3]";
//						req.service = "s";
						com.shadowgame.rpg.modules.protobuf.dto.RequestDtoPROTO.RequestDto req = com.shadowgame.rpg.modules.protobuf.dto.RequestDtoPROTO.RequestDto.
						newBuilder()
						.setMethod("next")
						.setSn(1)
						.setService("PlayerCtrl")
						.setParams("[]").build();
						f.getChannel().write(req);
					}
				}
			}
		}, "debug exit listener").start();
	}
}
