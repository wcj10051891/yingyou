package com.shadowgame.rpg.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.server.servlet.ServletServer;
import xgame.core.util.ClassUtils;
import xgame.core.util.Service;

import com.shadowgame.rpg.core.AppConfig;

public class ServletService implements Service {
	private static final Logger log = LoggerFactory.getLogger(ServletService.class);
	private ServletServer servletServer;

	@SuppressWarnings("unchecked")
	@Override
	public void start() throws Exception {
		//registry servlets
		Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>(2);
		for(Class<?> clazz : ClassUtils.scanPackage(AppConfig.SERVLET_PACKAGE)) {
			if(Servlet.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(WebServlet.class)) {
				WebServlet a = clazz.getAnnotation(WebServlet.class);
				String urlPattern = "/" + clazz.getSimpleName().toLowerCase();
				if(a.value().length > 0)
					urlPattern = a.value()[0];
				servlets.put(urlPattern, (Class<? extends Servlet>) clazz);
				log.debug("registry servlet:{}", clazz);
			}
		}
		servletServer = new ServletServer(AppConfig.HTTP_PORT, AppConfig.HTTP_DEFAULT_CHARSET, AppConfig.HTTP_CONTEXT_ROOT, null, servlets);
		servletServer.start();
	}

	@Override
	public void stop() throws Exception {
		servletServer.stop();
	}

}
