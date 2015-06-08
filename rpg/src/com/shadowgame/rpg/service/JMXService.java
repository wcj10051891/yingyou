package com.shadowgame.rpg.service;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MXBean;
import javax.management.ObjectName;

import xgame.core.util.ClassUtils;
import xgame.core.util.Service;

import com.shadowgame.rpg.core.AppConfig;

public class JMXService implements Service {

	private MBeanServer mBeanServer;
	private Map<Class<?>, Object> mxBeans;
	
	@Override
	public void start() throws Exception {
		mxBeans = new HashMap<Class<?>, Object>(0);
		mBeanServer = ManagementFactory.getPlatformMBeanServer();
		for (Class<?> clazz : ClassUtils.scanPackage(AppConfig.JMX_PACKAGE)) {
			for (Class<?> superClass : clazz.getInterfaces()) {
				if(superClass.isAnnotationPresent(MXBean.class) 
						&& superClass.getAnnotation(MXBean.class).value()) {
					Object mxBeanInstance = clazz.newInstance();
					mBeanServer.registerMBean(mxBeanInstance, 
						new ObjectName(clazz.getPackage().getName(), "name", clazz.getSimpleName()));
					mxBeans.put(clazz, mxBeanInstance);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getMXBean(Class<T> mxBeanImplClass) {
		return (T) this.mxBeans.get(mxBeanImplClass);
	}

	@Override
	public void stop() throws Exception {
	}

}
