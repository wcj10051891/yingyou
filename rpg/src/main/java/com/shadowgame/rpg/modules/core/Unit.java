package com.shadowgame.rpg.modules.core;

import xgame.core.event.BaseObservable;

import com.shadowgame.rpg.modules.scene.Zone;

public abstract class Unit extends BaseObservable {

	public enum UnitType {
		Player, Pet, Npc, EntryPoint, Monster;
	}

	public UnitType unitType;
	public Zone currentZone;
	
	public void onInSight(Unit unit) {
		
	}
	
	public void onOutSight(Unit unit) {
		
	}
}
