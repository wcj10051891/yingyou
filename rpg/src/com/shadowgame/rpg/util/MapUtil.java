package com.shadowgame.rpg.util;

import java.util.ArrayList;
import java.util.List;

import com.shadowgame.rpg.modules.map.GameMap;
import com.shadowgame.rpg.modules.map.Grid;
import com.shadowgame.rpg.modules.map.Point;

public abstract class MapUtil {
	
	/**
	 * 获取周围格子
	 */
	public static List<Grid> getAroundGrids(GameMap map, Grid current) {
		List<Grid> result = new ArrayList<Grid>();
		for(int i = current.x - 1; i <= current.x + 1; i++) {
			if(i < 0)
				continue;
			for(int j = current.y - 1; j <= current.y + 1; j++) {
				if(j < 0)
					continue;
				if(i == current.x && j == current.y)
					continue;
				result.add(map.getGrid(new Point(i, j)));
			}
		}
		return result;
	}
}
