package com.shadowgame.rpg.modules.exec;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import xgame.core.util.ObjectUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.core.AlertException;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.modules.core.Player;

public class Exec {
	private String key;
	private JSONObject param;
	private Object target;
	private Method targetMethod;
	
	public Exec(String key, Object object, String method, JSONObject param) {
		this.key = key;
		this.param = param;
		this.target = object;
		this.targetMethod = ObjectUtils.findMatchedMethod(object.getClass(), method, Arrays.asList(Player.class, JSONObject.class));
		this.targetMethod.setAccessible(true);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T invoke(Player player, JSONObject arg) {
		try {
			if(arg == null || arg.isEmpty())
				arg = param;
			return (T) targetMethod.invoke(target, player, arg);
		} catch (IllegalAccessException e) {
			throw new AppException("execution method error", e);
		} catch (IllegalArgumentException e) {
			throw new AppException("execution method error", e);
		} catch (InvocationTargetException e) {
			Throwable targetEx = e.getTargetException();
			if(targetEx instanceof AlertException)
				throw (AlertException)targetEx;
			else if(targetEx instanceof NoticeException)
				throw (NoticeException)targetEx;
			else if(targetEx instanceof AppException)
				throw (AppException)targetEx;
			else
				throw new AppException("execution method error", targetEx);
		}
	}

	public String getKey() {
		return key;
	}
}
