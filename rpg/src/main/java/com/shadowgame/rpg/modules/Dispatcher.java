package com.shadowgame.rpg.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.ClassUtils;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.core.Player;

public class Dispatcher {
	private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

	private Map<String, InstanceAndMethod> methods = new HashMap<String, InstanceAndMethod>();
	private static class InstanceAndMethod {
		public Object instance;
		public MethodAccess methodAccess;
		/**
		 * 
		 */
		public InstanceAndMethod(Object instance, MethodAccess methodAccess) {
			this.instance = instance;
			this.methodAccess = methodAccess;
		}
	}
	
	public Dispatcher() {
		try {
			for (Class<?> clazz : ClassUtils.scanPackage(AppConfig.MODULE_PACKAGE_NAME)) {
				if (clazz.isAnnotationPresent(Controller.class))
					methods.put(clazz.getSimpleName(), new InstanceAndMethod(clazz.newInstance(), MethodAccess.get(clazz)));
			}
		} catch (Exception e) {
			throw new AppException("dispatcher init error.", e);
		}
	}
	
	public Object dispatch(Player player, String service, String method, List<Object> params) throws Exception {
		if(player != null) {
			params = new ArrayList<Object>(params);
			params.add(0, player);
		}
		InstanceAndMethod objectAndMethod = this.methods.get(service);
		return objectAndMethod.methodAccess.invoke(objectAndMethod.instance, method, params.toArray());
	}
}
