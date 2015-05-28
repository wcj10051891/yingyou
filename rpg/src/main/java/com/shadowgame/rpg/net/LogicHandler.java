package com.shadowgame.rpg.net;

import java.lang.reflect.Method;

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
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.msg.AlertMsg;
import com.shadowgame.rpg.msg.LoginMsg;
import com.shadowgame.rpg.msg.NoticeMsg;
import com.shadowgame.rpg.net.msg.ClientMsg;
import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.service.Services;

@Sharable
public class LogicHandler extends SimpleChannelUpstreamHandler {
	private static final Logger log = LoggerFactory.getLogger(LogicHandler.class);
    
	@Override
	public void channelConnected(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//		Services.timerService.jdkScheduler.schedule(new Runnable() {
//			@Override
//			public void run() {
//				if(ctx.getAttachment() == null)
//					ctx.getChannel().close();
//			}
//		}, 10, TimeUnit.MINUTES);
		Services.tcpService.channels.put(ctx);
		log.info("player:{}, channel:{} connected", ctx.getAttachment(), ctx.getChannel());
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		Services.threadService.execute(new Runnable() {
			@Override
			public void run() {
				final Player player = (Player)ctx.getAttachment();
				final Message msg = (Message)e.getMessage();
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
		try {
			log.info("process client msg:{}, player:{}, channel:{}", msg, player, ctx.getChannel());
			if(msg instanceof LoginMsg) {//未登录，没有player
				Method loginMethod = msg.getClass().getMethod("handleLogin", ChannelHandlerContext.class);
				loginMethod.invoke(msg, ctx);
			} else {
				msg.handle(player);
			}
		} catch (Exception ex) {
			Throwable cause = ex.getCause();
			if(cause instanceof AlertException)
				Services.tcpService.send(new AlertMsg(((AlertException)cause).msg), ctx.getChannel());
			else if(cause instanceof NoticeException)
				Services.tcpService.send(new NoticeMsg(((NoticeException)cause).msg), ctx.getChannel());
			else {
				log.error("process client msg error, player:{}, channel:{}, msg:{}, error:{}", 
						player, ctx.getChannel(), msg, ex);
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error("player:{}, channel:{}, exception:{}", e.getChannel().getAttachment(), e.getChannel(), e.getCause());
	}

	@Override
	public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Services.threadService.execute(new Runnable() {
			@Override
			public void run() {
				Player player = (Player)ctx.getAttachment();
				if(player != null) {
					try {
						player.onLogout();
					} catch (Exception ex) {
						log.error("player:{}, channel:{} logout error:{}", player, ctx.getChannel(), ex.getCause());
					}
					ctx.setAttachment(null);
				}
				log.info("player:{}, channel:{} channelClosed", player, ctx.getChannel());
			}
		});
	}
}
