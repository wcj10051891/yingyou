package com.shadowgame.rpg.persist.entity;
/** 玩家 */
public class TPlayer{
	/** 玩家id */
	public Long id;
	/** 用户名 */
	public String username="";
	/** 昵称 */
	public String nickname="";
	/** 职业，1战士2法师3弓箭手 */
	public Integer vocation=0;
	/** 等级 */
	public Integer lv=0;
	/** 当前经验 */
	public Integer exp=0;
	/** 创建时间 */
	public java.sql.Timestamp createTime;
	/** 上次登录时间 */
	public java.sql.Timestamp loginTime;
	/** 上次登出时间 */
	public java.sql.Timestamp logoutTime;
	/** 每日属性 */
	public String daily;
	/** 扩展属性 */
	public String extAttribute;
	/** 离线所在地图配置id */
	public Integer lastMapId;
	/** 离线所在地图副本id */
	public Integer lastInstanceId;
	/** 离线所在副本坐标x */
	public Integer lastMapX;
	/** 离线所在副本坐标y */
	public Integer lastMapY;
	public Long getId(){
		return this.id;
	}
	public String getUsername(){
		return this.username;
	}
	public String getNickname(){
		return this.nickname;
	}
	public Integer getVocation(){
		return this.vocation;
	}
	public Integer getLv(){
		return this.lv;
	}
	public Integer getExp(){
		return this.exp;
	}
	public java.sql.Timestamp getCreateTime(){
		return this.createTime;
	}
	public java.sql.Timestamp getLoginTime(){
		return this.loginTime;
	}
	public java.sql.Timestamp getLogoutTime(){
		return this.logoutTime;
	}
	public String getDaily(){
		return this.daily;
	}
	public String getExtAttribute(){
		return this.extAttribute;
	}
	public Integer getLastMapId(){
		return this.lastMapId;
	}
	public Integer getLastInstanceId(){
		return this.lastInstanceId;
	}
	public Integer getLastMapX(){
		return this.lastMapX;
	}
	public Integer getLastMapY(){
		return this.lastMapY;
	}
	public void setId(Long id){
		this.id = id;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setNickname(String nickname){
		this.nickname = nickname;
	}
	public void setVocation(Integer vocation){
		this.vocation = vocation;
	}
	public void setLv(Integer lv){
		this.lv = lv;
	}
	public void setExp(Integer exp){
		this.exp = exp;
	}
	public void setCreateTime(java.sql.Timestamp createTime){
		this.createTime = createTime;
	}
	public void setLoginTime(java.sql.Timestamp loginTime){
		this.loginTime = loginTime;
	}
	public void setLogoutTime(java.sql.Timestamp logoutTime){
		this.logoutTime = logoutTime;
	}
	public void setDaily(String daily){
		this.daily = daily;
	}
	public void setExtAttribute(String extAttribute){
		this.extAttribute = extAttribute;
	}
	public void setLastMapId(Integer lastMapId){
		this.lastMapId = lastMapId;
	}
	public void setLastInstanceId(Integer lastInstanceId){
		this.lastInstanceId = lastInstanceId;
	}
	public void setLastMapX(Integer lastMapX){
		this.lastMapX = lastMapX;
	}
	public void setLastMapY(Integer lastMapY){
		this.lastMapY = lastMapY;
	}
}

