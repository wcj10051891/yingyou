package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 怪物信息
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午4:59:39
 */
public class MonsterInfo extends Message {
	/**
	 * 怪物模型id 
	 */
	public int modelId;
	/**
	 * 怪物唯一id 
	 */
	public long id;
	/**
	 * 怪物名称
	 */
	public String name;
	/**
	 * 坐标x
	 */
	public short x;
	/**
	 * 坐标y
	 */
	public short y;

	public MonsterInfo from(Monster monster) {
		this.modelId = monster.entity.id;
		this.id = monster.getObjectId();
		this.name = monster.entity.name;
		this.x = (short)monster.getPosition().getX();
		this.y = (short)monster.getPosition().getY();
		return this;
	}

}
