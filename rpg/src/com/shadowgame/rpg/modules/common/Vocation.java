package com.shadowgame.rpg.modules.common;

public enum Vocation {
	/**
	 * 战士
	 */
	warrior(1),
	/**
	 * 法师 
	 */
	master(2),
	/**
	 * 弓箭手 
	 */
	Archer(3);
	
	public int id;
	
	private Vocation(int id) {
		this.id = id;
	}
	
}
