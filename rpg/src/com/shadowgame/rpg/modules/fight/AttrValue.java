package com.shadowgame.rpg.modules.fight;


public class AttrValue {
	/**
	 * 属性类型
	 */
	private AttrType type;
	/**
	 * 原始值
	 */
	private int origin;
	/**
	 * 当前值
	 */
	private int value;

	public AttrValue(AttrType type, int value) {
		this.type = type;
		this.origin = value;
		this.value = value;
	}
	
	public void add(int value) {
		this.value += value;
	}
	
	public void set(int value) {
		this.value = value;
	}
	
	public void reset() {
		this.value = this.origin;
	}

	public AttrType getType() {
		return type;
	}

	public int getOrigin() {
		return origin;
	}

	public int getValue() {
		return value;
	}
}
