package com.shadowgame.rpg.msg.login;

import java.util.Arrays;

import org.jboss.netty.channel.ChannelHandlerContext;

import xgame.core.util.StopWatch;
import xgame.core.util.StringUtils;

import com.shadowgame.rpg.core.AlertException;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.map.MapInstance;
import com.shadowgame.rpg.net.msg.ClientMsg;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.UniqueId;

/**
 * 登录协议请求
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午4:36:44
 */
public class C_Login extends ClientMsg {
	/**
	 * 字节
	 */
	public byte byteValue;
	/**
	 * 短整型
	 */
	public short shortValue;
	/**
	 * 整型
	 */
	public int intValue;
	/**
	 * 长整型
	 */
	public long longValue;
	/**
	 * float浮点型
	 */
	public float floatValue;
	/**
	 * double浮点型
	 */
	public double doubleValue;
	/**
	 * 字符串
	 */
	public String stringValue;
	/**
	 * 数组[C_LoginAttachment]
	 */
	public java.util.List<C_LoginAttachment> nestValue1;
	/**
	 * 数组[Integer]
	 */
	public java.util.List<Integer> nestValue2;
	/**
	 * 数组[String]
	 */
	public java.util.List<String> nestValue3;
	
	
	@Override
	public void handle(Player player) {
		
	}
	

	public void handleLogin(ChannelHandlerContext ctx) {
		System.out.println("receive:" + StringUtils.toString(this));
		
		S_LoginResult r = new S_LoginResult();
		r.byteValue = 1;
		r.doubleValue = 2;
		r.floatValue = 3;
		r.intValue = 4;
		r.longValue = 5;
		r.nestValue1 = Arrays.asList("尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛", "尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛", "尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛尼玛");
		r.nestValue2 = Arrays.asList(new S_LoginResultAttachment("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"), new S_LoginResultAttachment("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"), new S_LoginResultAttachment("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"));
		r.shortValue = 6;
		r.stringValue = "呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵";
//		Services.tcpService.send(r, ctx.getChannel());
//		throw new AlertException("尼玛");
		
		Player player = Services.cacheService.get(4672337295849525248l, Player.class, true);
		player.onLogin(ctx.getChannel());
		MapInstance target = Services.app.world.gameMaps.get(1).getDefaultInstance();
		Services.app.world.updatePosition(player, target, 0, 0);
	}

}
