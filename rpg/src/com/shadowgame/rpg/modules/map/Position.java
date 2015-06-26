package com.shadowgame.rpg.modules.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.util.MapUtil;

/**
 * 世界地图上的位置
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:16:57
 */
public class Position {
	private static final Logger log = LoggerFactory.getLogger(Position.class);
	/**
	 * 所在区块
	 */
	private MapRegion mapRegion;
	/**
	 * 坐标点
	 */
	private Point point;
	/**
	 * 朝向，小键盘数字 7：↖， 4：←， 1：↙， 2：↓， 3：↘， 6：→，9：↗，8：↑
	 */
	private int direction = 2;
	
	public Position(int x, int y) {
		this.point = new Point(x, y);
	}

	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public int getX() {
		return this.point.x;
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public int getY() {
		return this.point.y;
	}

	/**
	 * 获取所在区块
	 * @return 	MapRegion
	 */
	public MapRegion getMapRegion() {
		return mapRegion;
	}
	
	/**
	 * 获取所在地图
	 * @return	MapInstance
	 */
	public MapInstance getMapInstance() {
		return mapRegion.getMapInstance();
	}

	/**
	 * 设置坐标
	 * @param owner			所属对象
	 * @param newInstance	坐标所属地图（旧地图或者新进入的地图）
	 * @param newX			新坐标x
	 * @param newY			新坐标y
	 */
	void update(MapObject owner, MapInstance newInstance, int newPointX, int newPointY) {
		Point old = this.point;
		this.point = new Point(newPointX, newPointY);
		if(old != null)
			this.direction = MapUtil.calcDirection(old, this.point);
		log.debug("{} update position {}", owner, this);
		//是否切换区域
		MapRegion newRegion = newInstance.getRegion(newPointX, newPointY);
		if (newRegion != mapRegion) {
			newRegion.add(owner);
			log.debug("{} change mapRegion from {} to {}", owner, mapRegion, newRegion);
			this.mapRegion = newRegion;
		}
	}

	public Point getPoint() {
		return point;
	}

	public int getDirection() {
		return direction;
	}
	
	@Override
	public String toString() {
		return String.valueOf(mapRegion) + ",point:" + getX() + "," + getY() + ",dir:" + direction;
	}
}
