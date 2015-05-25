package com.shadowgame.rpg.modules.core;

import xgame.core.event.BaseObservable;

import com.shadowgame.rpg.modules.world.KnownList;
import com.shadowgame.rpg.modules.world.MapRegion;
import com.shadowgame.rpg.modules.world.WorldPosition;

/**
 * 地图上的可见对象，视野内互相可见
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午6:47:54
 */
public abstract class VisibleObject extends BaseObservable {

	protected Long objectId;

	/**
	 * Position of object in the world.
	 */
	private WorldPosition position;

	/**
	 * KnownList of this VisibleObject.
	 */
	private KnownList knownlist;

	/**
	 * Visible object's target
	 */
	private VisibleObject target;

	public MapRegion getActiveRegion() {
		return position.getMapRegion();
	}

	public int getInstanceId() {
		return position.getInstanceId();
	}

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public int getWorldId() {
		return position.getMapId();
	}

	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public int getX() {
		return position.getX();
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public int getY() {
		return position.getY();
	}

	/**
	 * Return object position
	 * 
	 * @return position.
	 */
	public WorldPosition getPosition() {
		return position;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isInWorld() {
		return position.getWorld().findObject(getObjectId()) != null;
	}

	public void clearKnownlist() {
		getKnownList().clear();
	}

	public void updateKnownlist() {
		getKnownList().doUpdate();
	}

	/**
	 * Set KnownList to this VisibleObject
	 * 
	 * @param knownlist
	 */
	public void setKnownlist(KnownList knownlist) {
		this.knownlist = knownlist;
	}

	/**
	 * Returns KnownList of this VisibleObject.
	 * 
	 * @return knownList.
	 */
	public KnownList getKnownList() {
		return knownlist;
	}

	/**
	 * 
	 * @return VisibleObject
	 */
	public VisibleObject getTarget() {
		return target;
	}

	/**
	 * 
	 * @param creature
	 */
	public void setTarget(VisibleObject creature) {
		target = creature;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	/**
	 * Called when controlled object is seeing other VisibleObject.
	 * 
	 * @param object
	 */
	public void see(VisibleObject object) {

	}

	/**
	 * Called when controlled object no longer see some other VisibleObject.
	 * 
	 * @param object
	 */
	public void notSee(VisibleObject object, boolean isOutOfRange) {

	}
}
