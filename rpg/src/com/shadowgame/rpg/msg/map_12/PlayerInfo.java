package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 玩家信息
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午5:16:18
 */
public class PlayerInfo extends Message {
	/**
	 * 玩家id
	 */
	public long id;
	/**
	 * 玩家昵称
	 */
	public String name;
	/**
	 * 等级
	 */
	public int lv;
	/**
	 * 职业
	 */
	public byte vocation;
	/**
	 * 坐标x
	 */
	public short x;
	/**
	 * 坐标y
	 */
	public short y;
	
	public PlayerInfo from(Player player) {
		this.id = player.getKey();
		this.name = player.entity.nickname;
		this.lv = player.entity.lv;
		this.vocation = player.entity.vocation.byteValue();
		this.x = (short)player.getPosition().getX();
		this.y = (short)player.getPosition().getY();
		return this;
	}
}
