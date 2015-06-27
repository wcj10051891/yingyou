package com.shadowgame.rpg.persist.entity;
/** 玩家任务 */
public class TPlayerMission{
	/** 玩家id */
	public Long id;
	/** 已接任务 */
	public String acceptMission;
	/** 已完成任务 */
	public String finishMission;
	public Long getId(){
		return this.id;
	}
	public String getAcceptMission(){
		return this.acceptMission;
	}
	public String getFinishMission(){
		return this.finishMission;
	}
	public void setId(Long id){
		this.id = id;
	}
	public void setAcceptMission(String acceptMission){
		this.acceptMission = acceptMission;
	}
	public void setFinishMission(String finishMission){
		this.finishMission = finishMission;
	}
}

