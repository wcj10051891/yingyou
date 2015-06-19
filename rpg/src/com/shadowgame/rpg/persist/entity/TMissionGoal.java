package com.shadowgame.rpg.persist.entity;
/** 任务目标 */
public class TMissionGoal{
	/** 简称 */
	public String key="";
	/** 实现类 */
	public String clazz="";
	/** 默认参数 */
	public String param="";
	public String getKey(){
		return this.key;
	}
	public String getClazz(){
		return this.clazz;
	}
	public String getParam(){
		return this.param;
	}
	public void setKey(String key){
		this.key = key;
	}
	public void setClazz(String clazz){
		this.clazz = clazz;
	}
	public void setParam(String param){
		this.param = param;
	}
}

