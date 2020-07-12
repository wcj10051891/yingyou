package com.shadowgame.rpg.net.msg;

import com.shadowgame.rpg.modules.player.Player;

/**
 * 客户端发上来的消息
 * @author wcj10051891@gmail.com
 * @Date 2015年5月27日 上午10:14:36
 */
public abstract class ClientMsg extends Message {
	/**
	 * 处理客户端发送的消息
	 * @param player
	 */
	public abstract void handle(Player player);
}
