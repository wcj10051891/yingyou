package com.shadowgame.rpg.modules.core;

import com.shadowgame.rpg.modules.buff.BuffList;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.skill.SkillList;
import com.shadowgame.rpg.persist.entity.TMonster;
import com.shadowgame.rpg.util.UniqueId;


public class Monster extends AbstractFighter {
	public TMonster entity;

	public Monster(Position position, TMonster entity) {
		this.objectId = UniqueId.next();
		this.position = position;
		this.entity = entity;
		this.skillList = new SkillList(this);
		this.buffList = new BuffList(this);
		this.attrs = new FighterAttrs(this);
		//init attrs
		this.attrs.initAttr(AttrType.hp, 40);
		this.attrs.initAttr(AttrType.maxHp, 40);
		this.attrs.initAttr(AttrType.atk, 10);
		this.attrs.initAttr(AttrType.def, 5);
		this.attrs.initAttr(AttrType.damageFactor1, 1);
		this.attrs.initAttr(AttrType.damageFactor2, 1);
	}
}
