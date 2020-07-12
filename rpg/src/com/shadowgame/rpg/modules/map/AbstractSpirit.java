package com.shadowgame.rpg.modules.map;

import java.util.Collection;
import java.util.List;


/**
 * 可在地图上移动，改变位置的对象，有视野，看见其他对象
 * @author wcj10051891@gmail.com
 * @Date 2015年5月28日 下午12:18:58
 */
public abstract class AbstractSpirit extends MapObject {
	
	/**
	 * 移动管理器
	 */
	private MoveManager moveManager;
	
	public AbstractSpirit() {
		this.moveManager = new MoveManager(this);
	}
	
	/**
	 * 看见对象时候执行
	 * @param objects
	 */
	public void see(List<MapObject> objects) {
	}

	/**
	 * 看不见对象时候执行
	 * @param object
	 */
	public void notSee(List<MapObject> objects) {
//		System.out.println(this + " notSee " + object);
	}
	
	public void stopMoving() {
		//更新最新位置
//		Services.appService.world.updatePosition(this, this.position.getMapRegion().getMapInstance(), this.position.getX(), this.position.getY());
		//广播移动结束
		System.out.println("移动结束");
	}
	
	public int getSpeed() {
		return 100;
	}

	public MoveManager getMoveManager() {
		return moveManager;
	}
	
	/**
	 * 获取当前以及周围8个区块中所有的指定类型type的mapObject
	 * @param type
	 * @return
	 */
	public <T extends MapObject> Collection<T> getVisibilityObjectsByType(Class<T> type) {
		Collection<T> objects = this.position.getMapRegion().getVisibilityObjectsByType(type);
		objects.remove(this);
		return objects;
	}
	
	/**
	 * 获取当前以及周围8个区块中所有的mapObject
	 * @return
	 */
	public <T extends MapObject> Collection<T> getVisibilityObjects() {
		Collection<T> objects = this.position.getMapRegion().getVisibilityObjects();
		objects.remove(this);
		return objects;
	}
}
