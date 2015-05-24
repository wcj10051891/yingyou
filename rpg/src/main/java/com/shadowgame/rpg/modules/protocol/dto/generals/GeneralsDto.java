package com.shadowgame.rpg.modules.protocol.dto.generals;

/**
 * 武将dto
 * @author wcj10051891@gmail.com
 */
public class GeneralsDto {
	/**
	 * 武将id
	 */
	public long id;
	
	/**
	 * 家将基本信息
	 */
	public GeneralsSimpleDto generalsSimple;
	/**
	 * 部队星级
	 */
	public int army;
	
	/**
	 * 战法id
	 */
	public int skillId;
	/**
	 * 统
	 */
	public int tong;
	/**
	 * 勇
	 */
	public int yong;
	/**
	 * 智
	 */
	public int zhi;
	/**
	 * 阵中位置
	 */
	public int position;
}
