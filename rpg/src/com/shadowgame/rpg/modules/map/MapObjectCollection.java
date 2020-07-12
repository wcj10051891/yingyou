package com.shadowgame.rpg.modules.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapObjectCollection<V extends MapObject> extends ConcurrentHashMap<Long, V> {
	private static final long serialVersionUID = 1L;
	private Map<Class<V>, Map<Long, V>> type2Objects = new ConcurrentHashMap<Class<V>, Map<Long, V>>();
	@Override
	public V put(Long key, V value) {
		return putIfAbsent(key, value);
	}
	
	public Map<Long, V> getObjectsByType(Class<V> type) {
		Map<Long, V> map = type2Objects.get(type);
		if(map == null) {
			map = new HashMap<>();
			type2Objects.put(type, map);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V putIfAbsent(Long key, V value) {
		V result = super.putIfAbsent(key, value);
		if(result == null)
			getObjectsByType((Class<V>) value.getClass()).put(key, value);
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		V result = super.remove(key);
		if(result != null)
			getObjectsByType((Class<V>) result.getClass()).remove(key);
		return result;
	}
	
	@Override
	public void clear() {
		super.clear();
		type2Objects.clear();
	}
}