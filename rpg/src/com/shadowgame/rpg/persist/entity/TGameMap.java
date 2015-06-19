package com.shadowgame.rpg.persist.entity;
/** 地图 */
public class TGameMap{
	/** 地图id */
	public Integer id;
	/** 地图名称 */
	public String name="";
	/** 宽 */
	public Integer width=0;
	/** 高 */
	public Integer height=0;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public Integer getWidth(){
		return this.width;
	}
	public Integer getHeight(){
		return this.height;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setWidth(Integer width){
		this.width = width;
	}
	public void setHeight(Integer height){
		this.height = height;
	}
}

