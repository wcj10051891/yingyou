package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 移动消息广播
 * @author wcj10051891@gmail.com
 * @date 2015年7月6日 下午4:39:23
 */
public class Sc_12005 extends Message {
	/**
	 * 移动目标id
	 */
	public long id;
	/**
	 * 坐标x
	 */
	public short x;
	/**
	 * 坐标y
	 */
	public short y;
	
	public Sc_12005 from(AbstractFighter fighter) {
		this.id = fighter.getObjectId();
		this.x = (short)fighter.getPosition().getX();
		this.y = (short)fighter.getPosition().getY();
		return this;
	}
}
