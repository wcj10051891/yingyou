package com.shadowgame.rpg.persist.entity;
/** 道具 */
public class TItem{
	/** 道具id */
	public Integer id;
	/** 道具名称 */
	public String name="";
	/** 道具描述 */
	public String desc="";
	/** 需求玩家等级 */
	public Integer playerLv=0;
	/** 父分类 */
	public Integer parentType=0;
	/** 道具分类 */
	public Integer itemType=0;
	/** 绑定类型（0不绑定1获得绑定2使用绑定） */
	public Integer bindType=0;
	/** 品质 */
	public Integer quality=0;
	/** 最大堆叠数 */
	public Integer maxStack=0;
	/** 道具功能列表 */
	public String function="";
	/** 扩展属性 */
	public String extAttribute;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getDesc(){
		return this.desc;
	}
	public Integer getPlayerLv(){
		return this.playerLv;
	}
	public Integer getParentType(){
		return this.parentType;
	}
	public Integer getItemType(){
		return this.itemType;
	}
	public Integer getBindType(){
		return this.bindType;
	}
	public Integer getQuality(){
		return this.quality;
	}
	public Integer getMaxStack(){
		return this.maxStack;
	}
	public String getFunction(){
		return this.function;
	}
	public String getExtAttribute(){
		return this.extAttribute;
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
	public void setPlayerLv(Integer playerLv){
		this.playerLv = playerLv;
	}
	public void setParentType(Integer parentType){
		this.parentType = parentType;
	}
	public void setItemType(Integer itemType){
		this.itemType = itemType;
	}
	public void setBindType(Integer bindType){
		this.bindType = bindType;
	}
	public void setQuality(Integer quality){
		this.quality = quality;
	}
	public void setMaxStack(Integer maxStack){
		this.maxStack = maxStack;
	}
	public void setFunction(String function){
		this.function = function;
	}
	public void setExtAttribute(String extAttribute){
		this.extAttribute = extAttribute;
	}
}

