package com.shadowgame.rpg.msg.skill_13;

import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 技能施放确认
 * @author wcj10051891@gmail.com
 * @date 2015年7月10日 上午11:33:42
 */
public class Sc_13000 extends Message {
	/**
	 * 施放者id
	 */
	public long id;
	/**
	 * 技能id
	 */
	public int skillId;
	
	public Sc_13000 from(AbstractFighter fighter, int skillId) {
		this.id = fighter.getObjectId();
		this.skillId = skillId;
		return this;
	}
}
