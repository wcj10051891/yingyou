package com.shadowgame.rpg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.ClassUtils;
import xgame.core.util.ObjectUtils;
import xgame.core.util.Service;

import com.shadowgame.rpg.core.AbstractData;
import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;

public class DataService implements Service {
	private static final Logger log = LoggerFactory.getLogger(DataService.class);
	private Map<Class<? extends AbstractData>, AbstractData> configs = new HashMap<Class<? extends AbstractData>, AbstractData>();

	@SuppressWarnings("unchecked")
	@Override
	public void start() throws Exception {
		List<Class<? extends AbstractData>> context = new ArrayList<Class<? extends AbstractData>>();
		for (Class<?> clazz : ClassUtils.scanPackage(AppConfig.DATA_PACKAGE)) {
			if(AbstractData.class.isAssignableFrom(clazz))
				init((Class<? extends AbstractData>)clazz, context);
		}
	}
	
	private void init(Class<? extends AbstractData> clazz, List<Class<? extends AbstractData>> context) {
		if(context.contains(clazz))
			throw new AppException("cyclic dependency error:" + context + ", current init:" + clazz);
		context.add(clazz);
		
		AbstractData dataObject = this.configs.get(clazz);
		if(dataObject == null)
			dataObject = ObjectUtils.create(clazz);
		
		Class<? extends AbstractData>[] depends = dataObject.depends();
		if(depends == null || depends.length == 0) {
			dataObject.load();
			this.configs.put(clazz, dataObject);
			log.info("init data:{}", clazz);
		} else {
			for (Class<? extends AbstractData> depend : depends) {
				try {
					init(depend, context);
				} catch (AppException e) {
					throw e;
				} catch (Exception e) {
					throw new AppException("dataConfig depends must exists and annotated by " + Data.class + ", and not allow cyclic dependency", e);
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
