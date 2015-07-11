package com.shadowgame.rpg.modules.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 属性
 * @author wcj10051891@gmail.com
 * @date 2015年7月10日 下午3:31:53
 */
public class FighterAttrs {
	private Map<AttrType, AttrValue> attrs;
	private AbstractFighter fighter;
	private ReentrantReadWriteLock lock;

	public FighterAttrs(AbstractFighter fighter) {
		this.fighter = fighter;
		this.attrs = new ConcurrentHashMap<AttrType, AttrValue>();
		this.lock = new ReentrantReadWriteLock();
		for (AttrType type : AttrType.values())
			attrs.put(type, new AttrValue(type, 0));
	}

	public void initAttr(AttrType type, int value) {
		if (!attrs.containsKey(type)) {
			attrs.put(type, new AttrValue(type, value));
		} else {
			attrs.get(type).reset();
			attrs.get(type).set(value);
		}
	}

	public void set(AttrType type, int value) {
		lock.writeLock().lock();
		try {
			if (!attrs.containsKey(type))
				attrs.put(type, new AttrValue(type, 0));
			
			attrs.get(type).set(value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public int get(AttrType type) {
		int value = 0;
		lock.readLock().lock();
		try {
			if (attrs.containsKey(type))
				value = attrs.get(type).getValue();
		} finally {
			lock.readLock().unlock();
		}
		return value;
	}

	public void resetStats() {
		for (AttrValue value : this.attrs.values())
			value.reset();
	}

	public AbstractFighter getFighter() {
		return fighter;
	}
}
