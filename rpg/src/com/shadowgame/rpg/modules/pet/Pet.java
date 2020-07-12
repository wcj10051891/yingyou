package com.shadowgame.rpg.modules.pet;

import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.util.UniqueId;


public class Pet extends AbstractFighter {
	
	public Pet() {
		this.objectId = UniqueId.next();
	}

}
