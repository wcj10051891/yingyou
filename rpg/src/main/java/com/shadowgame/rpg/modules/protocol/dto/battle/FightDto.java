package com.shadowgame.rpg.modules.protocol.dto.battle;

import java.util.List;

/**
 * 攻击dto
 * @author wcj10051891@gmail.com
 */
public class FightDto {
	/**
	 * 攻击者
	 */
	public int sourceId;
	/**
	 * 被攻击者
	 */
	public int targetId;
	/**
	 * 使用技能id
	 * 0为普攻
	 */
	public int skillId;
	/**
	 * 攻击结果
	 */
	public List<FightResultDto> result;
	
	/**
	 * 增加士气
	 */
	public int detlaMorale;
}
