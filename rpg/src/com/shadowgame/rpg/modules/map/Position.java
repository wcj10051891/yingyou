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
	 * 坐标点
	 */
	private Point point;
	/**
	 * 方向
	 */
	private int heading;
	
	public Position(int x, int y) {
		this.point = new Point(x, y);
	}
	
	public Position(Point point) {
		this.point = point;
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
	void setNewPoint(MapObject owner, MapInstance newInstance, Point newPoint) {
		this.point = newPoint;
		//是否切换区域
		MapRegion newRegion = newInstance.getRegion(newPoint);
		if (newRegion != mapRegion) {
			newRegion.add(owner);
			this.mapRegion = newRegion;
		}
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

	public Point getPoint() {
		return point;
	}
}
