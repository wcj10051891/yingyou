package com.shadowgame.rpg.persist.entity;
/** 玩家 */
public class Player{
	/** 玩家id */
	public Long id;
	/** 用户名 */
	public String username="";
	/** 昵称 */
	public String nickname="";
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
	public Long getId(){
		return this.id;
	}
	public String getUsername(){
		return this.username;
	}
	public String getNickname(){
		return this.nickname;
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
	public void setId(Long id){
		this.id = id;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setNickname(String nickname){
		this.nickname = nickname;
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
}

