package com.shadowgame.rpg.modules.fight;

import com.shadowgame.rpg.modules.buff.BuffList;
import com.shadowgame.rpg.modules.map.AbstractSpirit;
import com.shadowgame.rpg.modules.skill.SkillList;


/**
 * 可参战单位
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractFighter extends AbstractSpirit {
	/**
	 * 目标
	 */
	private AbstractFighter target;
	/**
	 * 技能列表
	 */
	protected SkillList skillList;
	/**
	 * buff列表
	 */
	protected BuffList buffList;
	/**
	 * 战斗属性
	 */
	protected FighterAttrs attrs;
	/**
	 * 仇恨列表
	 */
	protected HateList hateList;
	
	
	public boolean isDied() {
		return this.attrs.get(AttrType.hp) == 0;
	}
	
	public AbstractFighter getTarget() {
		return target;
	}
	
	public void setTarget(AbstractFighter target) {
		this.target = target;
	}

	public SkillList getSkillList() {
		return skillList;
	}

	public void setSkillList(SkillList skillList) {
		this.skillList = skillList;
	}

	public BuffList getBuffList() {
		return buffList;
	}

	public void setBuffList(BuffList buffList) {
		this.buffList = buffList;
	}

	public FighterAttrs getAttrs() {
		return attrs;
	}

	public void setAttrs(FighterAttrs attrs) {
		this.attrs = attrs;
	}

	public HateList getHateList() {
		return hateList;
	}

	public void setHateList(HateList hateList) {
		this.hateList = hateList;
	}
}
