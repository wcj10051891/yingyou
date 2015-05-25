package com.shadowgame.rpg.modules.world;

/**
 * 世界地图上的位置
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:16:57
 */
public class WorldPosition {
	/**
	 * Map Region.
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
	 * Return World map id.
	 * 
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
	 * Return map region
	 * 
	 * @return Map region
	 */
	public MapRegion getMapRegion() {
		return mapRegion;
	}

	/**
	 * 
	 * @return
	 */
	public int getInstanceId() {
		return mapRegion.getParent().getInstanceId();
	}

	/**
	 * 
	 * @return
	 */
	public int getInstanceCount() {
		return mapRegion.getParent().getParent().getInstanceCount();
	}

	/**
	 * Returns the {@link World} instance in which this position is located. :D
	 * 
	 * @return World
	 */
	public World getWorld() {
		return mapRegion.getWorld();
	}

	/**
	 * Set map region
	 * 
	 * @param r
	 *            - map region
	 */
	void setMapRegion(MapRegion r) {
		mapRegion = r;
	}

	void setXY(int newX, int newY) {
		x = newX;
		y = newY;
	}
}
