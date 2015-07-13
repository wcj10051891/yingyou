package com.shadowgame.rpg.modules.monster;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.impl.NormalMonsterAI;
import com.shadowgame.rpg.modules.buff.BuffList;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.fight.AttrType;
import com.shadowgame.rpg.modules.fight.FighterAttrs;
import com.shadowgame.rpg.modules.fight.HateList;
import com.shadowgame.rpg.modules.fight.LifeAttrs;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.skill.SkillList;
import com.shadowgame.rpg.persist.entity.TMonster;
import com.shadowgame.rpg.util.UniqueId;


public class Monster extends AbstractFighter {
	public TMonster entity;
	private AbstractAI ai;

	public Monster(Position position, TMonster entity) {
		this.objectId = UniqueId.next();
		this.position = position;
		this.entity = entity;
		this.skillList = new SkillList(this);
		this.buffList = new BuffList(this);
		this.hateList = new HateList(this);
		this.fightAttrs = new FighterAttrs(this);
		//init attrs
		this.fightAttrs.initAttr(AttrType.maxHp, 40);
		this.fightAttrs.initAttr(AttrType.atk, 10);
		this.fightAttrs.initAttr(AttrType.def, 5);
		this.fightAttrs.initAttr(AttrType.damageFactor1, 1);
		this.fightAttrs.initAttr(AttrType.damageFactor2, 1);

		this.lifeAttrs = new LifeAttrs(this, this.fightAttrs.get(AttrType.maxHp), 0);
		
		this.ai = new NormalMonsterAI(this);
	}
	
	public AbstractAI getAI() {
		return this.ai;
	}
	
	public boolean isAtBornPoint() {
		return this.position.getX() == entity.bornX && this.position.getY() == entity.bornY;
	}
}
