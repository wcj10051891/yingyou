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
	 * Owner of this KnownList.
	 */
	protected final VisibleObject owner;
	/**
	 * List of objects that this KnownList owner known
	 */
	protected final ConcurrentHashMap<Long, VisibleObject> knownObjects = new ConcurrentHashMap<Long, VisibleObject>();

	/**
	 * COnstructor.
	 * 
	 * @param owner
	 */
	public KnownList(VisibleObject owner) {
		this.owner = owner;
	}

	/**
	 * Do KnownList update.
	 */
	public void doUpdate() {
		forgetObjects();
		findVisibleObjects();
	}

	/**
	 * Clear known list. Used when object is despawned.
	 */
	public void clear() {
		for (Iterator<VisibleObject> knownIt = iterator(); knownIt.hasNext();) {
			VisibleObject obj = knownIt.next();
			knownIt.remove();
			obj.getKnownList().del(owner, false);
		}
	}

	/**
	 * Check if object is known
	 * 
	 * @param object
	 * @return true if object is known
	 */
	public boolean knowns(VisibleObject object) {
		return knownObjects.containsKey(object.getObjectId());
	}

	/**
	 * Returns an iterator over VisibleObjects on this known list
	 * 
	 * @return objects iterator
	 */
	@Override
	public Iterator<VisibleObject> iterator() {
		return knownObjects.values().iterator();
	}

	/**
	 * Add VisibleObject to this KnownList.
	 * 
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
	 * Delete VisibleObject from this KnownList.
	 * 
	 * @param object
	 */
	private void del(VisibleObject object, boolean isOutOfRange) {
		/**
		 * object was known.
		 */
		if (knownObjects.remove(object.getObjectId()) != null)
			owner.notSee(object, isOutOfRange);
	}

	/**
	 * forget out of distance objects.
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
	 * Find objects that are in visibility range.
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

	protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject) {
//		return MathUtil.isInRange(owner, newObject, VisibilityDistance);
		for (MapRegion mr : owner.getActiveRegion().getNeighbours()) {
			if(mr.getRegionId().equals(newObject.getActiveRegion().getRegionId()))
				return true;
		}
		return false;
	}
}
