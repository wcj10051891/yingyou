package com.shadowgame.rpg.core;


/**
 * 数据基础类，用于存数据库加载的实体entity，用EntityMap来保存这些entity，支持reload
 * @author wcj10051891@gmail.com
 * @date 2015年7月9日 下午7:16:15
 */
public abstract class AbstractData {
	public abstract void load();
	
	public Class<? extends AbstractData>[] depends() {
		return null;
	}
}
