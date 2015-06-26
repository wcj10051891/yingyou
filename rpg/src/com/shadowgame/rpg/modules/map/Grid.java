package com.shadowgame.rpg.modules.map;


/**
 * 地图方格
 * @author wcj10051891@gmail.com
 * @date 2015年6月25日 下午4:57:15
 */
public class Grid {
	/**
	 * 格子边长
	 */
	public static int SIZE = 25;
	/**
	 * 格子坐标x
	 */
	public int x;
	/**
	 * 格子坐标y
	 */
	public int y;
	/**
	 * 该格子的中心点坐标
	 */
	public Point center;
	/**
	 * 是否是阻挡，不可行走
	 */
	public boolean block;
	
	public Grid(int x, int y, Point center, boolean block) {
		super();
		this.x = x;
		this.y = y;
		this.center = center;
		this.block = block;
	}
	
	@Override
	public boolean equals(Object obj) {
		Grid t = (Grid)obj;
		return t.x == x && t.y == y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
}
