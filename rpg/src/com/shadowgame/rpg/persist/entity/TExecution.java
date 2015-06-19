package com.shadowgame.rpg.persist.entity;
/** 执行 */
public class TExecution{
	/** 简称 */
	public String key="";
	/** 类名 */
	public String clazz="";
	/** 方法名 */
	public String method="";
	/** 默认参数 */
	public String param="";
	public String getKey(){
		return this.key;
	}
	public String getClazz(){
		return this.clazz;
	}
	public String getMethod(){
		return this.method;
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
	public void setMethod(String method){
		this.method = method;
	}
	public void setParam(String param){
		this.param = param;
	}
}

