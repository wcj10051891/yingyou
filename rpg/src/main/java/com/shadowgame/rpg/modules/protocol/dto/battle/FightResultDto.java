package com.shadowgame.rpg.modules.protocol.dto.battle;

/**
 * 攻击结果
 * @author wcj10051891@gmail.com
 */
public class FightResultDto {
	/**
	 * 攻击结果类型
	 * 1扣血
	 * 2闪避
	 * 3抵抗
	 * 4暴击
	 */
	public int type;
	
	/**
	 * 目标武将id
	 */
	public int targetId;
	
	/**
	 * 扣血值
	 */
	public int value;
}
