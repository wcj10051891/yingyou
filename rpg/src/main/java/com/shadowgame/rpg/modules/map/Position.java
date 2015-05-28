package com.shadowgame.rpg.modules.map;

import com.shadowgame.rpg.modules.core.MapObject;

/**
 * 世界地图上的位置
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:16:57
 */
public class Position {
	/**
	 * 所在区块
	 */
	private MapRegion mapRegion;
	/**
	 * World position x
	 */
	private int x;
	/**
	 * World position y
	 */
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * 获取所在区块
	 * @return Map region
	 */
	public MapRegion getMapRegion() {
		return mapRegion;
	}

	/**
	 * 设置坐标
	 * @param owner			所属对象
	 * @param newInstance	坐标所属地图（旧地图或者新进入的地图）
	 * @param newX			新坐标x
	 * @param newY			新坐标y
	 */
	void setXY(MapObject owner, MapInstance newInstance, int newX, int newY) {
		this.x = newX;
		this.y = newY;
		//是否切换区域
		MapRegion newRegion = newInstance.getRegion(newX, newY);
		if (newRegion != mapRegion) {
			newRegion.add(owner);
			this.mapRegion = newRegion;
		}
	}
}
