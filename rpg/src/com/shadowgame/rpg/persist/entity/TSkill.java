package com.shadowgame.rpg.persist.entity;
/** 技能 */
public class TSkill{
	/** 技能id */
	public Integer id;
	/** 技能名称 */
	public String name="";
	/** 描述 */
	public String description="";
	/** 职业id */
	public Integer vocation=0;
	/** 攻击距离 */
	public Integer distance=0;
	/** 目标 */
	public Integer target=0;
	/** 技能参数 */
	public String param;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getDescription(){
		return this.description;
	}
	public Integer getVocation(){
		return this.vocation;
	}
	public Integer getDistance(){
		return this.distance;
	}
	public Integer getTarget(){
		return this.target;
	}
	public String getParam(){
		return this.param;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setVocation(Integer vocation){
		this.vocation = vocation;
	}
	public void setDistance(Integer distance){
		this.distance = distance;
	}
	public void setTarget(Integer target){
		this.target = target;
	}
	public void setParam(String param){
		this.param = param;
	}
}

