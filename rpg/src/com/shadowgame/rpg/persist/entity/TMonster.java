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
	public Integer x=0;
	/** 坐标y */
	public Integer y=0;
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
	public Integer getX(){
		return this.x;
	}
	public Integer getY(){
		return this.y;
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
	public void setX(Integer x){
		this.x = x;
	}
	public void setY(Integer y){
		this.y = y;
	}
}

