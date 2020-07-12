package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;
import com.shadowgame.rpg.service.Services;

/**
 * 移动消息
 * @author wcj10051891@gmail.com
 * @date 2015年7月6日 下午4:39:23
 */
public class Cs_12000 extends ClientMsg {
	/**
	 * 坐标x
	 */
	public short x;
	/**
	 * 坐标y
	 */
	public short y;
	
	@Override
	public void handle(Player player) {
		Services.app.world.updatePosition(player, x, y);
	}
}
