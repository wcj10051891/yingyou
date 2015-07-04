package com.shadowgame.rpg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.ClassUtils;
import xgame.core.util.ObjectUtils;
import xgame.core.util.Service;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.core.Data;

public class DataService implements Service {
	private static final Logger log = LoggerFactory.getLogger(DataService.class);
	private Map<Class<?>, Object> configs = new HashMap<Class<?>, Object>();

	@Override
	public void start() throws Exception {
		this.configs.clear();
		List<Class<?>> context = new ArrayList<Class<?>>();
		for (Class<?> clazz : ClassUtils.scanPackage(AppConfig.DATA_PACKAGE)) {
			if(clazz.isAnnotationPresent(Data.class)) {
				init(clazz, clazz.getAnnotation(Data.class), context);
			}
		}
	}
	
	private void init(Class<?> clazz, Data cfg, List<Class<?>> context) {
		if(!this.configs.containsKey(clazz)) {
			if(context.contains(clazz))
				throw new AppException("cyclic dependency error:" + context + ", current init:" + clazz);
			context.add(clazz);
			Class<?>[] depends = cfg.depends();
			if(depends.length == 0) {
				this.configs.put(clazz, ObjectUtils.create(clazz));
				log.debug("init data:{}", clazz);
			} else {
				for (Class<?> depend : depends) {
					try {
						init(depend, depend.getAnnotation(Data.class), context);
					} catch (AppException e) {
						throw e;
					} catch (Exception e) {
						throw new AppException("dataConfig depends must exists and annotated by " + Data.class + ", and not allow cyclic dependency", e);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		return (T) this.configs.get(clazz);
	}

	@Override
	public void stop() throws Exception {
	}
}
