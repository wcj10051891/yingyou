package com.shadowgame.rpg.persist.entity;
/** 怪物表 */
public class TMonster{
	/** 怪物id */
	public Integer id;
	/** 怪物名称 */
	public String name="";
	/** 是否是boss */
	public Boolean boss=false;
	/** 所属地图id */
	public Integer mapId=0;
	/** 坐标x */
	public Integer bornX=0;
	/** 坐标y */
	public Integer bornY=0;
	/** 追击距离 */
	public Integer followDistance=0;
	/** 离出生点距离 */
	public Integer bornDistance=0;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public Boolean getBoss(){
		return this.boss;
	}
	public Integer getMapId(){
		return this.mapId;
	}
	public Integer getBornX(){
		return this.bornX;
	}
	public Integer getBornY(){
		return this.bornY;
	}
	public Integer getFollowDistance(){
		return this.followDistance;
	}
	public Integer getBornDistance(){
		return this.bornDistance;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setBoss(Boolean boss){
		this.boss = boss;
	}
	public void setMapId(Integer mapId){
		this.mapId = mapId;
	}
	public void setBornX(Integer bornX){
		this.bornX = bornX;
	}
	public void setBornY(Integer bornY){
		this.bornY = bornY;
	}
	public void setFollowDistance(Integer followDistance){
		this.followDistance = followDistance;
	}
	public void setBornDistance(Integer bornDistance){
		this.bornDistance = bornDistance;
	}
}

