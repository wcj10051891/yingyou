package com.shadowgame.rpg.modules.core;

import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.persist.entity.TMonster;


public class Monster extends AbstractFighter {
	public TMonster entity;

	public Monster(Position position, TMonster entity) {
		this.position = position;
		this.entity = entity;
	}
}
