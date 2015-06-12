package com.shadowgame.rpg.msg;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.modules.core.Player;
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
	public String username;
	/**
	 * 密码
	 */
	public String password;
	/**
	 * 提示消息数组
	 */
	public List<NoticeMsg> msgs1;
	/**
	 * 提示消息数组
	 */
	public List<AlertMsg> msgs2;
	
	@Override
	public void handle(Player player) {
	}
	

	public void handleLogin(ChannelHandlerContext ctx) {
//		Services.tcpService.send(new AlertMsg("登录成功"), ctx.getChannel());
		LoginResult r = new LoginResult();
		r.d = 1111111111d;
		r.f = 2f;
		r.i = 3;
		r.l = 444444444l;
		r.s = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";
		r.r2 = new ArrayList<LoginResult2>();
		LoginResult2 r2 = new LoginResult2();
		r2.d = 555555555d;
		r2.f = 6666666666f;
		r2.i = 77777777;
		r2.l = 88888888l;
		r2.s = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";
		
		LoginResult2 r3 = new LoginResult2();
		r3.d = 555555555d;
		r3.f = 6666666666f;
		r3.i = 77777777;
		r3.l = 88888888l;
		r3.s = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";
		
		r.r2.add(r2);
		r.r2.add(r3);
		
		r2.r3 = new ArrayList<LoginResult3>();
		LoginResult3 l = new LoginResult3();
		l.s = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";
		r2.r3 = new ArrayList<LoginResult3>();
		r2.r3.add(l);

		LoginResult3 l2 = new LoginResult3();
		l2.s = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";
		r3.r3 = new ArrayList<LoginResult3>();
		r3.r3.add(l2);

		Services.tcpService.send(r, ctx.getChannel());
		
		
		
		
		// Services.tcpService.send(this, ctx.getChannel());
//		Player p = new Player();
//		p.channel = ctx.getChannel();
//		p.channel.setAttachment(p);
//		p.setObjectId(Long.valueOf(p.channel.getId()));
//		p.setPosition(new Position(0, 0));
//		Services.appService.world.updatePosition(p, Services.appService.world.getWorldMap(1).getDefaultInstance(), 0, 0);
//		p.entity = new com.shadowgame.rpg.persist.entity.Player();
//		p.entity.username = "user" + p.getObjectId();
//		p.entity.nickname = "nick" + p.getObjectId();
//		Services.appService.world.addObject(p);
//		Services.appService.world.updatePosition(p, Services.appService.world.getWorldMap(1).getDefaultInstance(), 350, 350);
//		p.send(new AlertMsg("player " + p.getKey() + " login success, enter mapRegion:" + StringUtils.toString(p.getPosition())));

//		p.getPosition().getMapRegion().broadcast(new NoticeMsg("大家好我来了"), p);
	}
}
