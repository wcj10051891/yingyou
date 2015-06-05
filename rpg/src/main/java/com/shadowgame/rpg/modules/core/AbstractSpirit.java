package com.shadowgame.rpg.modules.core;

/**
 * 可在地图上移动，改变位置的对象，有视野，看见其他对象
 * @author wcj10051891@gmail.com
 * @Date 2015年5月28日 下午12:18:58
 */
public abstract class AbstractSpirit extends MapObject {
	/**
	 * 看见对象时候执行
	 * @param object
	 */
	public void see(MapObject object) {
//		System.out.println(this + " see " + object);
	}

	/**
	 * 看不见对象时候执行
	 * @param object
	 */
	public void notSee(MapObject object) {
//		System.out.println(this + " notSee " + object);
	}
}
