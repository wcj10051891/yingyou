package com.shadowgame.rpg.net;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AlertException;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.jmx.impl.Statistics;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.msg.core_10.Sc_10000;
import com.shadowgame.rpg.net.msg.ClientMsg;
import com.shadowgame.rpg.net.msg.NoPlayerClientMsg;
import com.shadowgame.rpg.service.Services;

@Sharable
public class LogicHandler extends SimpleChannelUpstreamHandler {
	private static final Logger log = LoggerFactory.getLogger(LogicHandler.class);
	
	private Statistics stat = Services.jmxService.getMXBean(Statistics.class);
	@Override
	public void channelConnected(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//		Services.timerService.jdkScheduler.schedule(new Runnable() {
//			@Override
//			public void run() {
//				if(ctx.getChannel().getAttachment() == null)
//					ctx.getChannel().close();
//			}
//		}, 10, TimeUnit.MINUTES);
//		
//		Services.threadService.execute(new Runnable() {
//			@Override
//			public void run() {
////				Services.tcpService.joinGroup(Groups.World, ctx.getChannel());
//				log.info("player:{}, channel:{} connected", ctx.getChannel().getAttachment(), ctx.getChannel());
//			}
//		});
		stat.users.incrementAndGet();
		log.info("channel:{} channelConnected", ctx.getChannel());
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		Services.threadService.execute(new Runnable() {
			@Override
			public void run() {
				final Player player = (Player)ctx.getChannel().getAttachment();
				final Object msg = e.getMessage();
				if(!(msg instanceof ClientMsg))
					throw new AppException("receive msg not instanceof ClientMsg:[" + msg + 
							"], server can't process, player:" + player + ", channel:" + ctx.getChannel());
				if(player == null) {
					processRequest(player, (ClientMsg)msg, ctx);
				} else {  
					//每个玩家一个队列执行当前玩家的请求
					player.processQueue.execute(new Runnable() {
						@Override
						public void run() {
							processRequest(player, (ClientMsg)msg, ctx);
						}
					});
				}
			}
		});
	}
	
	private void processRequest(Player player, ClientMsg msg, ChannelHandlerContext ctx) {
		long start = System.currentTimeMillis();
		try {
			log.info("process client msg:{}, player:{}, channel:{}", msg, player, ctx.getChannel());
			if(msg instanceof NoPlayerClientMsg) {
				msg.getClass().getMethod("handleNoPlayer", ChannelHandlerContext.class).invoke(msg, ctx);
			} else {
				msg.handle(player);
			}
			stat.requestCount.incrementAndGet();
		} catch (Exception ex) {
			Throwable cause = ex.getCause();
			if(cause != null) {
				if(cause instanceof AlertException) {
					Services.tcpService.send(new Sc_10000(1, ((AlertException)cause).msg), ctx.getChannel());
					return;
				} else if(cause instanceof NoticeException) {
					Services.tcpService.send(new Sc_10000(((NoticeException)cause).msg), ctx.getChannel());
					return;
				}
			}
			log.error("process client msg error, player:{}, channel:{}, msg:{}, error:{}", 
					player, ctx.getChannel(), msg, ex);
			ex.printStackTrace();
		}
		long cost = System.currentTimeMillis() - start;
		stat.responseTimeSum.getAndAdd(cost);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error("player:{}, channel:{}, exception:{}", e.getChannel().getAttachment(), e.getChannel(), e.getCause());
		e.getCause().printStackTrace();
	}

	@Override
	public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Services.threadService.execute(new Runnable() {
			@Override
			public void run() {
				Player player = (Player)ctx.getChannel().getAttachment();
				if(player != null) {
					try {
						player.onLogout();
					} catch (Exception ex) {
						throw new AppException("logout error", ex);
					} finally {
						player.channel = null;
						ctx.getChannel().setAttachment(null);
					}
				}
				log.info("player:{}, channel:{} channelClosed", player, ctx.getChannel());
			}
		});
		stat.users.decrementAndGet();
	}
}
