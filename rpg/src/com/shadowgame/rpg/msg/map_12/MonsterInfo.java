package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.core.Monster;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 怪物信息
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午4:59:39
 */
public class MonsterInfo extends Message {
	/**
	 * 怪物id 
	 */
	public int id;
	/**
	 * 怪物名称
	 */
	public String name;

	public MonsterInfo from(Monster monster) {
		this.id = monster.getObjectId();
		this.name = monster.entity.name;
		return this;
	}

}
