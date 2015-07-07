package com.shadowgame.rpg.persist.entity;
/** 技能 */
public class TSkill{
	/** 技能id */
	public Integer id;
	/** 技能名称 */
	public String name="";
	/** 描述 */
	public String desc="";
	/** 职业id */
	public Integer vocation=0;
	/** 作用范围类型，1矩形2圆形3扇形 */
	public Integer rangeType=0;
	/** 作用范围位置，1施法者2目标 */
	public Integer rangePosition=0;
	/** 作用范围距离 */
	public Integer rangeDistance=0;
	/** 角度 */
	public Integer rangeAngle=0;
	/** 宽度 */
	public Integer rangeWidth=0;
	/** 延迟时间，毫秒 */
	public Integer delay=0;
	/** 作用间隔，毫秒 */
	public Integer period=0;
	/** 作用次数 */
	public Integer count=0;
	/** 技能参数 */
	public String param;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getDesc(){
		return this.desc;
	}
	public Integer getVocation(){
		return this.vocation;
	}
	public Integer getRangeType(){
		return this.rangeType;
	}
	public Integer getRangePosition(){
		return this.rangePosition;
	}
	public Integer getRangeDistance(){
		return this.rangeDistance;
	}
	public Integer getRangeAngle(){
		return this.rangeAngle;
	}
	public Integer getRangeWidth(){
		return this.rangeWidth;
	}
	public Integer getDelay(){
		return this.delay;
	}
	public Integer getPeriod(){
		return this.period;
	}
	public Integer getCount(){
		return this.count;
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
	public void setDesc(String desc){
		this.desc = desc;
	}
	public void setVocation(Integer vocation){
		this.vocation = vocation;
	}
	public void setRangeType(Integer rangeType){
		this.rangeType = rangeType;
	}
	public void setRangePosition(Integer rangePosition){
		this.rangePosition = rangePosition;
	}
	public void setRangeDistance(Integer rangeDistance){
		this.rangeDistance = rangeDistance;
	}
	public void setRangeAngle(Integer rangeAngle){
		this.rangeAngle = rangeAngle;
	}
	public void setRangeWidth(Integer rangeWidth){
		this.rangeWidth = rangeWidth;
	}
	public void setDelay(Integer delay){
		this.delay = delay;
	}
	public void setPeriod(Integer period){
		this.period = period;
	}
	public void setCount(Integer count){
		this.count = count;
	}
	public void setParam(String param){
		this.param = param;
	}
}

