package com.shadowgame.rpg.persist.entity;
/** 玩家技能 */
public class TPlayerSkill{
	/** 玩家id */
	public Long id;
	/** 技能信息 */
	public String skills;
	public Long getId(){
		return this.id;
	}
	public String getSkills(){
		return this.skills;
	}
	public void setId(Long id){
		this.id = id;
	}
	public void setSkills(String skills){
		this.skills = skills;
	}
}

