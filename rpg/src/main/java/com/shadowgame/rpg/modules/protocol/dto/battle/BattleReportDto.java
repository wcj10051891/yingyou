package com.shadowgame.rpg.modules.protocol.dto.battle;

import java.util.List;

import com.shadowgame.rpg.modules.protocol.dto.generals.GeneralsDto;

/**
 * 战报dto
 * @author wcj10051891@gmail.com
 */
public class BattleReportDto {
	/**
	 * 目标名称
	 */
	public String targetName;
	/**
	 * 目标等级
	 */
	public int targetLv;
	/**
	 * 目标血量
	 */
	public int targetHp;
	/**
	 * 目标出战家将列表
	 */
	public List<GeneralsDto> targetGenerals;
	/**
	 * 战斗回合过程
	 */
	public List<FightRoundDto> rounds;
	
	/**
	 * 是否胜利
	 */
	public boolean win;
	
	/**
	 * 星级
	 */
	public int star;
	
	/**
	 * 我方损失兵力
	 */
	public int loseHp;
	
	/**
	 * 获得军功
	 */
	public int exploit;
	
	/**
	 * 获得装备
	 */
	public String itemName;
}
