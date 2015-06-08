package com.shadowgame.rpg.persist.entity;
/** 道具 */
public class Item{
	/** 道具id */
	public Integer id;
	/** 名称 */
	public String name="";
	/** 道具描述 */
	public String description;
	/** 需求玩家等级 */
	public Integer playerLevel;
	/** 父分类 */
	public Integer parentType;
	/** 道具分类 */
	public Integer itemType;
	/** 绑定类型（0不绑定1获得绑定2使用绑定） */
	public Integer bindType;
	/** 品质 */
	public Integer quality;
	/** 最大堆叠数 */
	public Integer maxStack;
	/** 道具功能列表 */
	public String function;
	/** 参数 */
	public String extAttribute;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getDescription(){
		return this.description;
	}
	public Integer getPlayerLevel(){
		return this.playerLevel;
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
	public void setDescription(String description){
		this.description = description;
	}
	public void setPlayerLevel(Integer playerLevel){
		this.playerLevel = playerLevel;
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

