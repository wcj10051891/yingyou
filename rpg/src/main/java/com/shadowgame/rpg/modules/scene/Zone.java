package com.shadowgame.rpg.modules.scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.shadowgame.rpg.modules.core.Unit;
import com.shadowgame.rpg.modules.core.Unit.UnitType;
import com.shadowgame.rpg.service.Services;

public class Zone {
	public static final int width = 400;
	public static final int height = 300;
	public String id;
	public Map<UnitType, Set<Unit>> units;
	public Scene scene;
	public Set<Zone> groupZones;
	public String groupName;

	public Zone(Scene scene, String id) {
		this.scene = scene;
		this.id = id;
		this.units = new HashMap<UnitType, Set<Unit>>();
		this.groupName = "zoneGroup_" + scene.getKey() + "_" + id;
	}
	
	public void broadcast(Object msg) {
		Services.tcpService.broadcast(msg, groupName);
	}
	
	public void add(Unit... addUnits) {
		for(Unit unit : addUnits) {
			Set<Unit> us = units.get(unit.unitType);
			if(us == null) {
				us = new HashSet<Unit>();
				units.put(unit.unitType, us);
			}
			unit.currentZone = this;
			us.add(unit);
		}
	}
	
	public void remove(Unit... removeUnits) {
		for(Unit unit : removeUnits) {
			Set<Unit> us = units.get(unit.unitType);
			if(us != null) {
				us.remove(unit);
				unit.currentZone = null;
			}
		}
	}
}
