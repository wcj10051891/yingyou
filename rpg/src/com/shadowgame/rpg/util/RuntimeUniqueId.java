package com.shadowgame.rpg.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器内运行时唯一id，每次开服从1递增，可变的，不用于保存到数据库
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午3:54:47
 */
public abstract class RuntimeUniqueId {
	private static Map<Class<?>, AtomicInteger> ids = new HashMap<Class<?>, AtomicInteger>(); 
	
	public static Integer next(Class<?> subject) {
		AtomicInteger id = ids.get(subject);
		if(id == null) {
			id = new AtomicInteger(1);
			ids.put(subject, id);
		}
		return id.getAndIncrement();
	}
}
