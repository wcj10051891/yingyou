package com.shadowgame.rpg.persist.entity;
/** buff配置表 */
public class TBuff{
	/** buff唯一id */
	public Integer id;
	/** buff名称 */
	public String name="";
	/** buff描述 */
	public String desc="";
	/** 参数 */
	public String param;
	/** 作用间隔，毫秒 */
	public Integer period=0;
	/** 作用次数 */
	public Integer count=0;
	/** 持续时间，毫秒 */
	public Integer duration=0;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getDesc(){
		return this.desc;
	}
	public String getParam(){
		return this.param;
	}
	public Integer getPeriod(){
		return this.period;
	}
	public Integer getCount(){
		return this.count;
	}
	public Integer getDuration(){
		return this.duration;
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
	public void setParam(String param){
		this.param = param;
	}
	public void setPeriod(Integer period){
		this.period = period;
	}
	public void setCount(Integer count){
		this.count = count;
	}
	public void setDuration(Integer duration){
		this.duration = duration;
	}
}

