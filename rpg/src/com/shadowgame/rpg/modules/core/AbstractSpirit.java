package com.shadowgame.rpg.modules.core;



/**
 * 可在地图上移动，改变位置的对象，有视野，看见其他对象
 * @author wcj10051891@gmail.com
 * @Date 2015年5月28日 下午12:18:58
 */
public abstract class AbstractSpirit extends MapObject {
	
	/**
	 * 目标
	 */
	private AbstractSpirit target;
	/**
	 * 移动管理器
	 */
	private MoveManager moveManager;
	
	public AbstractSpirit() {
		this.moveManager = new MoveManager(this);
	}
	
	/**
	 * 看见对象时候执行
	 * @param object
	 */
	public void see(MapObject object) {
	}

	/**
	 * 看不见对象时候执行
	 * @param object
	 */
	public void notSee(MapObject object) {
//		System.out.println(this + " notSee " + object);
	}
	
	public void stopMoving() {
		//更新最新位置
//		Services.appService.world.updatePosition(this, this.position.getMapRegion().getMapInstance(), this.position.getX(), this.position.getY());
		//广播移动结束
		System.out.println("移动结束");
	}

	public AbstractSpirit getTarget() {
		return target;
	}
	
	public void setTarget(AbstractSpirit target) {
		this.target = target;
	}
	
	public int getSpeed() {
		return 100;
	}

	public MoveManager getMoveManager() {
		return moveManager;
	}
}
