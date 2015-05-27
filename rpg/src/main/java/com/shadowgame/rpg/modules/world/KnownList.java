package com.shadowgame.rpg.modules.world;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.shadowgame.rpg.modules.core.VisibleObject;

/**
 * 对象的可见集合，视野
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:18:27
 */
public class KnownList implements Iterable<VisibleObject> {
	/**
	 * Visibility distance.
	 */
	// how far player will see visible object
//	private static final int VisibilityDistance = 95;

	/**
	 * 所属对象
	 */
	protected final VisibleObject owner;
	/**
	 * 视野里有哪些对象
	 */
	protected final ConcurrentHashMap<Long, VisibleObject> knownObjects = new ConcurrentHashMap<Long, VisibleObject>();

	public KnownList(VisibleObject owner) {
		this.owner = owner;
	}

	/**
	 * 根据对象当前位置来更新视野内的对象
	 */
	public void doUpdate() {
		forgetObjects();
		findVisibleObjects();
	}

	/**
	 * 移除视野内的所有对象
	 */
	public void clear() {
		for (Iterator<VisibleObject> knownIt = iterator(); knownIt.hasNext();) {
			VisibleObject obj = knownIt.next();
			knownIt.remove();
			obj.getKnownList().del(owner, false);
		}
	}

	/**
	 * 视野内是否包含指定对象
	 * @param object
	 * @return true if object is known
	 */
	public boolean knowns(VisibleObject object) {
		return knownObjects.containsKey(object.getObjectId());
	}

	/**
	 * 返回视野内对象的迭代器
	 * @return objects iterator
	 */
	@Override
	public Iterator<VisibleObject> iterator() {
		return knownObjects.values().iterator();
	}

	/**
	 * 添加对象
	 * @param object
	 */
	protected void add(VisibleObject object) {
		/**
		 * object is not known.
		 */
		if (knownObjects.put(object.getObjectId(), object) == null)
			owner.see(object);
	}

	/**
	 * 移除对象
	 * @param object
	 * @param isOutOfRange	是否是因为不在视野范围内而移除
	 */
	private void del(VisibleObject object, boolean isOutOfRange) {
		/**
		 * object was known.
		 */
		if (knownObjects.remove(object.getObjectId()) != null)
			owner.notSee(object, isOutOfRange);
	}

	/**
	 * 移除之前在视野里但目前不在视野内的对象
	 */
	private void forgetObjects() {
		for (Iterator<VisibleObject> knownIt = iterator(); knownIt.hasNext();) {
			VisibleObject obj = knownIt.next();
			if (!checkObjectInRange(owner, obj)) {
				knownIt.remove();
				owner.notSee(obj, true);
				obj.getKnownList().del(owner, true);
			}
		}
	}

	/**
	 * 添加在视野内但还没有加入视野对象列表的对象
	 */
	protected void findVisibleObjects() {
		if (owner == null)
			return;

		for (MapRegion r : owner.getActiveRegion().getNeighbours()) {
			for (VisibleObject newObject : r.getObjects()) {
				if (newObject == owner || newObject == null)
					continue;
				if (knownObjects.putIfAbsent(newObject.getObjectId(), newObject) == null) {
					newObject.getKnownList().add(owner);
					owner.see(newObject);
				}
			}
		}
	}

	/**
	 * 检查对象是否在可视范围
	 * @param owner
	 * @param newObject
	 * @return
	 */
	protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject) {
//		return MathUtil.isInRange(owner, newObject, VisibilityDistance);
		for (MapRegion mr : owner.getActiveRegion().getNeighbours()) {
			if(mr.getRegionId().equals(newObject.getActiveRegion().getRegionId()))
				return true;
		}
		return false;
	}
}
