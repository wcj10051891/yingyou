package com.shadowgame.rpg.modules.scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scene {

	private Map<String, Zone> zones;
	private int width;
	private int height;

	public void init() {
		zones = new HashMap<String, Zone>();
		int rows = height / Zone.height;
		int columns = width / Zone.width;
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				String id = r + "_" + c;
				zones.put(id, new Zone(this, id));
			}
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				Zone current = zones.get(r + "_" + c);
				Set<Zone> groupZones = new HashSet<Zone>(8);
				int r1 = r - 1;
				int r2 = r + 1;
				int c1 = c - 1;
				int c2 = c + 1;
				if (r1 >= 0) {
					groupZones.add(zones.get(r1 + "_" + c));
					if (c1 >= 0)
						groupZones.add(zones.get(r1 + "_" + c1));
					if (c2 >= 0)
						groupZones.add(zones.get(r1 + "_" + c2));
				}
				if (r2 >= 0) {
					groupZones.add(zones.get(r2 + "_" + c));
					if (c1 >= 0)
						groupZones.add(zones.get(r2 + "_" + c1));
					if (c2 >= 0)
						groupZones.add(zones.get(r2 + "_" + c2));
				}
				if (c1 >= 0)
					groupZones.add(zones.get(r + "_" + c1));
				if (c2 >= 0)
					groupZones.add(zones.get(r + "_" + c2));
				
				current.groupZones = groupZones;
			}
	}
	
	public Zone getZone(int x, int y) {
		return getZone(x / width + "_" + y / height);
	}

	public Zone getZone(String id) {
		return zones.get(id);
	}
	
	public Integer getKey() {
		return 0;
	}
}
