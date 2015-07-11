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
	/** 伤害间隔，毫秒 */
	public Integer period=0;
	/** 伤害次数 */
	public Integer count=0;
	/** 持续时间，毫秒 */
	public Integer duration=0;
	/** 固定伤害 */
	public Integer fixDamage=0;
	/** 基于生命上限伤害百分比 */
	public Integer hpMaxDamage=0;
	/** 普通伤害百分比 */
	public Integer normalDamage=0;
	/** 伤害增加百分比 */
	public Integer damageIncrease=0;
	/** 伤害减少百分比 */
	public Integer damageDecrease=0;
	/** 受到伤害增加百分比 */
	public Integer hitDamageIncrease=0;
	/** 受到伤害减少百分比 */
	public Integer hitDamageDecrease=0;
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
	public Integer getFixDamage(){
		return this.fixDamage;
	}
	public Integer getHpMaxDamage(){
		return this.hpMaxDamage;
	}
	public Integer getNormalDamage(){
		return this.normalDamage;
	}
	public Integer getDamageIncrease(){
		return this.damageIncrease;
	}
	public Integer getDamageDecrease(){
		return this.damageDecrease;
	}
	public Integer getHitDamageIncrease(){
		return this.hitDamageIncrease;
	}
	public Integer getHitDamageDecrease(){
		return this.hitDamageDecrease;
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
	public void setFixDamage(Integer fixDamage){
		this.fixDamage = fixDamage;
	}
	public void setHpMaxDamage(Integer hpMaxDamage){
		this.hpMaxDamage = hpMaxDamage;
	}
	public void setNormalDamage(Integer normalDamage){
		this.normalDamage = normalDamage;
	}
	public void setDamageIncrease(Integer damageIncrease){
		this.damageIncrease = damageIncrease;
	}
	public void setDamageDecrease(Integer damageDecrease){
		this.damageDecrease = damageDecrease;
	}
	public void setHitDamageIncrease(Integer hitDamageIncrease){
		this.hitDamageIncrease = hitDamageIncrease;
	}
	public void setHitDamageDecrease(Integer hitDamageDecrease){
		this.hitDamageDecrease = hitDamageDecrease;
	}
}

