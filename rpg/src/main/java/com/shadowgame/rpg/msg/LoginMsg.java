package com.shadowgame.rpg.msg;

import java.util.List;

import org.jboss.netty.channel.ChannelHandlerContext;

import xgame.core.net.protocol.Msg;
import xgame.core.util.StringUtils;

import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.net.msg.ClientMsg;
import com.shadowgame.rpg.service.Services;

/**
 * 登录消息
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月27日 上午10:03:53
 */
@Msg(10000)
public class LoginMsg extends ClientMsg {
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 弹窗提示
	 */
	private AlertMsg alertMsg;

	/**
	 * 飘字提示
	 */
	private List<NoticeMsg> noticeMsgs;

	/**
	 * 字符串消息
	 */
	private List<String> msgs;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void handleLogin(ChannelHandlerContext ctx) {
		// Services.tcpService.send(this, ctx.getChannel());
		Player p = new Player();
		p.channel = ctx.getChannel();
		p.channel.setAttachment(p);
		p.setObjectId(Long.valueOf(p.channel.getId()));
		p.setPosition(new Position(0, 0));
		Services.appService.world.updatePosition(p, Services.appService.world
				.getWorldMap(1).getDefaultInstance(), 0, 0);
		p.entity = new com.shadowgame.rpg.persist.entity.Player();
		p.entity.username = "user" + p.getObjectId();
		p.entity.nickname = "nick" + p.getObjectId();
		Services.appService.world.addObject(p);
		Services.appService.world.updatePosition(p, Services.appService.world
				.getWorldMap(1).getDefaultInstance(), 350, 350);
		p.send(new AlertMsg("player " + p.getKey()
				+ " login success, enter mapRegion:"
				+ StringUtils.toString(p.getPosition())));

		p.getPosition().getMapRegion().broadcast(new NoticeMsg("大家好我来了"), p);
	}

	public AlertMsg getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(AlertMsg alertMsg) {
		this.alertMsg = alertMsg;
	}

	public List<NoticeMsg> getNoticeMsgs() {
		return noticeMsgs;
	}

	public void setNoticeMsgs(List<NoticeMsg> noticeMsgs) {
		this.noticeMsgs = noticeMsgs;
	}

	public List<String> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<String> msgs) {
		this.msgs = msgs;
	}

}
