package com.shadowgame.rpg.modules.fight;

public enum AttrType {
	/**
	 * 生命最大值
	 */
	maxHp,
	/**
	 * 魔法最大值
	 */
	maxMp,
	/**
	 * 攻击
	 */
	atk,
	/**
	 * 防御
	 */
	def,
	
	/**
	 * 命中率
	 */
	hit,
	/**
	 * 闪避率
	 */
	dodge,
	/**
	 * 暴击率
	 */
	critRate,
	/**
	 * 抗暴率
	 */
	critDefRate,
	/**
	 * 暴击伤害
	 */
	crit,
	/**
	 * 暴击伤害减免
	 */
	critDef,
	/**
	 * 破击率
	 */
	breakBlockRate,
	/**
	 * 格挡率
	 */
	blockRate,
	/**
	 * 吸血几率
	 */
	suckHpRate,
	/**
	 * 吸血比例
	 */
	suckHpRatio,
	/**
	 * 反弹几率
	 */
	reboundRate,
	/**
	 * 反弹比例
	 */
	reboundRatio,

	/**
	 * 火焰伤害
	 */
	fireDamage,
	/**
	 * 寒冰伤害
	 */
	iceDamage,
	/**
	 * 雷电伤害
	 */
	thunderDamage,
	/**
	 * 毒素伤害
	 */
	poisonDamage,
	/**
	 * 神圣伤害
	 */
	holyDamage,
	/**
	 * 暗影伤害
	 */
	shadowDamage,
	/**
	 * 火焰抗性
	 */
	fireDef,
	/**
	 * 寒冰抗性
	 */
	iceDef,
	/**
	 * 雷电抗性
	 */
	thunderDef,
	/**
	 * 毒素抗性
	 */
	poisonDef,
	/**
	 * 神圣抗性
	 */
	holyDef,
	/**
	 * 暗影抗性
	 */
	shadowDef,
	
	/**
	 * 伤害加成
	 */
	damageIncrease,
	/**
	 * 伤害减免
	 */
	damageDecrease,

	/**
	 * 输出系数
	 */
	damageFactor1,
	/**
	 * 受伤系数
	 */
	damageFactor2,

	/**
	 * 眩晕值
	 */
	dizziness,
	/**
	 * 减速值
	 */
	slow,
	/**
	 * 冻结值
	 */
	freeze,
	/**
	 * 沉默值
	 */
	silence,
	/**
	 * 恐惧值
	 */
	fear,
	/**
	 * 中毒值
	 */
	poisoning,
	/**
	 * 抗眩晕
	 */
	dizzinessDef,
	/**
	 * 抗减速
	 */
	slowDef,
	/**
	 * 抗冻结
	 */
	freezeDef,
	/**
	 * 抗沉默
	 */
	silenceDef,
	/**
	 * 抗恐惧
	 */
	fearDef,
	/**
	 * 抗中毒
	 */
	poisoningDef;
}
