package com.shadowgame.rpg.modules.map;

public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return x == ((Point)obj).x && y == ((Point)obj).y;
	}
}
