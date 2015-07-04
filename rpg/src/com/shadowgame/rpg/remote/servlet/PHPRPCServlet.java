package com.shadowgame.rpg.remote.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import xgame.core.net.remote.servlet.PhpRpcDispatcher;
import xgame.core.util.ClassUtils;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;

@WebServlet("/rpc/*")
public class PHPRPCServlet extends PhpRpcDispatcher {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			for (Class<?> beanClass : ClassUtils.scanPackage(AppConfig.PHPRPC_BEAN_PACKAGE)) {
				this.registerBean(beanClass);
			}
		} catch (Exception e) {
			throw new AppException("register php rpc bean error", e);
		}
	}

}
