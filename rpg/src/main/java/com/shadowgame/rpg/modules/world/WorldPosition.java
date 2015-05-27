package com.shadowgame.rpg.modules.world;

/**
 * 世界地图上的位置
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:16:57
 */
public class WorldPosition {
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

	/**
	 * 获取地图ID
	 * @return world map id
	 */
	public int getMapId() {
		return mapRegion.getMapId();
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
	 * 获取地图副本id
	 * @return
	 */
	public int getInstanceId() {
		return mapRegion.getWorldMapInstance().getInstanceId();
	}

	/**
	 * 获取所属地图一共有多少个默认副本
	 * @return
	 */
	public int getTwinCount() {
		return mapRegion.getWorldMapInstance().getWorldMap().getTwinCount();
	}

	/**
	 * 获取世界
	 * @return World
	 */
	public World getWorld() {
		return mapRegion.getWorld();
	}

	/**
	 * 设置区块
	 * @param r
	 *            - map region
	 */
	void setMapRegion(MapRegion r) {
		mapRegion = r;
	}

	/**
	 * 设置坐标
	 * @param newX
	 * @param newY
	 */
	void setXY(int newX, int newY) {
		x = newX;
		y = newY;
	}
}
