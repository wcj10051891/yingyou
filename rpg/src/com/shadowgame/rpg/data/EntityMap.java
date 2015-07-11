package com.shadowgame.rpg.data;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库实体entity专用map，当data类reload时，如果map中已经存在，则copy最新属性赋值，对象不变，因为这个entity有其他地方引用到，更新属性值就好了
 * @author wcj10051891@gmail.com
 * @date 2015年7月9日 下午6:53:38
 */
public class EntityMap<K, V> extends ConcurrentHashMap<K, V>{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(EntityMap.class);
	
	@Override
	public V put(K key, V value) {
		return putIfAbsent(key, value);
	}
	
	@Override
	public V putIfAbsent(K key, V value) {
		V v = super.putIfAbsent(key, value);
		if(v != null) {
			try {
				BeanUtils.copyProperties(v, value);
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.error("reload entity properties error", e);
			}
		}
		return v;
	}
}
