package com.shadowgame.rpg.modules.map;


/**
 * 地图上的对象
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月28日 上午11:44:55
 */
public abstract class MapObject {
	/**
	 * 对象唯一id
	 */
	protected Long objectId;
	/**
	 * 位置
	 */
	protected Position position;

	public Long getObjectId() {
		return objectId;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public World getWorld() {
		return this.getGameMap().getWorld();
	}
	
	public GameMap getGameMap() {
		return this.position.getMapRegion().getMapInstance().getGameMap();
	}
	
	/**
	 * 第一次添加进地图时调用
	 * @param map
	 */
	public void onEnterMap(MapInstance map) {
		
	}
	
	/**
	 * 从地图移除时候调用
	 * @param map
	 */
	public void onLeaveMap(MapInstance map) {
		
	}
}
