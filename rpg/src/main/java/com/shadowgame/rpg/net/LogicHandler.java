package com.shadowgame.rpg.net;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.shadowgame.rpg.core.AlertException;
import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.jmx.impl.Statistics;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.protobuf.dto.RequestDtoPROTO.RequestDto;
import com.shadowgame.rpg.modules.protocol.dto.core.AlertDto;
import com.shadowgame.rpg.modules.protocol.dto.core.NoticeDto;
import com.shadowgame.rpg.modules.protocol.service.PlayerCtrl;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.ProtobufUtils;

@Sharable
public class LogicHandler extends SimpleChannelUpstreamHandler {
	private static final Logger log = LoggerFactory.getLogger(LogicHandler.class);
    public static String crossdomain = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";
    
	@Override
	public void channelConnected(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//		Services.timerService.jdkScheduler.schedule(new Runnable() {
//			@Override
//			public void run() {
//				if(ctx.getAttachment() == null)
//					ctx.getChannel().close();
//			}
//		}, 10, TimeUnit.MINUTES);
		log.info("client {} connected.", ctx.getChannel());
		Services.jmxService.getMXBean(Statistics.class).users.addAndGet(1);
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		Services.threadService.execute(new Runnable() {
			@Override
			public void run() {
				Services.jmxService.getMXBean(Statistics.class).requestCount.addAndGet(1);
				long start = System.currentTimeMillis();
				final Player player = (Player)ctx.getAttachment();
				final RequestDto request = (RequestDto)e.getMessage();
				try {
					if(player == null) {
//						player==null表示没有login，当login成功后，需要把player和socket channel关联绑定起来，
//						用threadLocal实现，所以这个线程和login方法调用必须在同一线程
						PlayerCtrl.loginChannel.set(ctx);
						processRequest(player, request, ctx);
					} else {
						//每个玩家一个队列执行当前玩家的请求
						player.processQueue.execute(new Runnable() {
							@Override
							public void run() {
								processRequest(player, request, ctx);
							}
						});
					}
				} finally {
					if(player == null)
						PlayerCtrl.loginChannel.remove();
				}
				long end = System.currentTimeMillis();
				Services.jmxService.getMXBean(Statistics.class).responseTimeSum.addAndGet((int)(end - start));
			}
		});
	}
	
	private void processRequest(Player player, RequestDto request, ChannelHandlerContext ctx) {
		String service = request.getService();
		String method = request.getMethod();
		String params = request.getParams();
		int sn = request.getSn();
		Object result = null;
		boolean isError = false;
		try {
			log.info("request from player {}, channel {}, service:{}, method:{}, params:{}.", 
				player, ctx.getChannel(), service, method, params);
			result = Services.appService.dispatcher.dispatch(player, service, method, JSONArray.parseArray(params));
		} catch (Exception ex) {
			isError = true;
			if(AlertException.class.isAssignableFrom(ex.getClass())) {
				result = new AlertDto(((AlertException)ex).msg);
			} else if(NoticeException.class.isAssignableFrom(ex.getClass())) {
				result = new NoticeDto(((NoticeException)ex).msg);
			}
			ex.printStackTrace();
			log.error("request exception, player {}, channel {}, service:{}, method:{}, params:{}, error:{}.", 
					player, ctx.getChannel(), service, method, params, ex);
		}
		if(result != null) {
			Services.tcpService.send(ProtobufUtils.toResponseDto(sn, isError, result), ctx.getChannel());
//			Services.tcpService.send(new ResponseDto(), ctx.getChannel());
			log.info("response to player {}, channel {}, service:{}, method:{}, params:{}, response:{}.", 
					player, ctx.getChannel(), service, method, params, result);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
		log.error("client {} exception: {}", e.getChannel(), e.getCause());
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
					} catch (Exception e2) {
						log.error("player logout error.", e2);
					}
					ctx.setAttachment(null);
					log.info("client {} channelClosed, player:{}.", ctx.getChannel(), player.getObjectId());
				} else {
					log.info("client {} channelClosed.", ctx.getChannel());
				}
			}
		});
	}
}
