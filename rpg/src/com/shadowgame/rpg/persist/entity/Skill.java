package com.shadowgame.rpg.persist.entity;
/** 技能 */
public class Skill{
	/** 技能id */
	public Integer id;
	/** 名称 */
	public String name;
	/** 描述 */
	public String description;
	/** 职业 */
	public Integer vocation;
	/** 攻击距离 */
	public Integer distance;
	/** 攻击目标 */
	public Integer target;
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

