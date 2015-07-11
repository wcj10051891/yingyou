package com.shadowgame.rpg.modules.core;

import com.shadowgame.rpg.modules.buff.BuffList;
import com.shadowgame.rpg.modules.skill.SkillList;


/**
 * 可参战单位
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractFighter extends AbstractSpirit {
	
	/**
	 * 技能列表
	 */
	public SkillList skillList;
	/**
	 * buff列表
	 */
	public BuffList buffList;
	/**
	 * 战斗属性
	 */
	public FighterAttrs attrs;
}
