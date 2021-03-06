package com.shadowgame.rpg.persist.entity;
/** 玩家道具 */
public class TPlayerItem{
	/** 玩家道具id */
	public Long id;
	/** 系统道具id */
	public Integer itemId=0;
	/** 玩家id */
	public Long playerId=0l;
	/** 是否绑定 */
	public Boolean binding=false;
	/** 堆叠数 */
	public Integer num=0;
	/** 强化等级 */
	public Integer strengthenLv=0;
	/** 宝石镶嵌孔 */
	public String hole;
	/** 创建时间 */
	public java.sql.Timestamp createTime=new java.sql.Timestamp(System.currentTimeMillis());
	public Long getId(){
		return this.id;
	}
	public Integer getItemId(){
		return this.itemId;
	}
	public Long getPlayerId(){
		return this.playerId;
	}
	public Boolean getBinding(){
		return this.binding;
	}
	public Integer getNum(){
		return this.num;
	}
	public Integer getStrengthenLv(){
		return this.strengthenLv;
	}
	public String getHole(){
		return this.hole;
	}
	public java.sql.Timestamp getCreateTime(){
		return this.createTime;
	}
	public void setId(Long id){
		this.id = id;
	}
	public void setItemId(Integer itemId){
		this.itemId = itemId;
	}
	public void setPlayerId(Long playerId){
		this.playerId = playerId;
	}
	public void setBinding(Boolean binding){
		this.binding = binding;
	}
	public void setNum(Integer num){
		this.num = num;
	}
	public void setStrengthenLv(Integer strengthenLv){
		this.strengthenLv = strengthenLv;
	}
	public void setHole(String hole){
		this.hole = hole;
	}
	public void setCreateTime(java.sql.Timestamp createTime){
		this.createTime = createTime;
	}
}

