package com.shadowgame.rpg.persist.entity;
/** 玩家背包 */
public class PlayerKnapsack{
	/** 玩家id */
	public Long id;
	/** 道具列表 */
	public String items;
	/** 当前容量 */
	public Integer capacity;
	public Long getId(){
		return this.id;
	}
	public String getItems(){
		return this.items;
	}
	public Integer getCapacity(){
		return this.capacity;
	}
	public void setId(Long id){
		this.id = id;
	}
	public void setItems(String items){
		this.items = items;
	}
	public void setCapacity(Integer capacity){
		this.capacity = capacity;
	}
}

