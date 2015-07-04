package com.shadowgame.rpg.modules.core;

import com.shadowgame.rpg.modules.buff.BuffList;
import com.shadowgame.rpg.modules.skill.SkillList;


/**
 * 可参战单位
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractFighter extends AbstractSpirit {
	
	/**
	 * 技能列表
	 */
	public SkillList skillList;
	/**
	 * buff列表
	 */
	public BuffList buffList;
	
	/**
	 * 当前生命
	 */
	public int hp;
	/**
	 * 生命最大值
	 */
	public int maxhp;
	/**
	 * 攻击
	 */
	public int atk;
	/**
	 * 防御
	 */
	public int def;
	
	/**
	 * 命中率
	 */
	public int hit;
	/**
	 * 闪避率
	 */
	public int dodge;
	/**
	 * 暴击率
	 */
	public int critRate;
	/**
	 * 抗暴率
	 */
	public int critDefRate;
	/**
	 * 暴击伤害
	 */
	public int crit;
	/**
	 * 暴击伤害减免
	 */
	public int critDef;
	/**
	 * 破击率
	 */
	public int breakBlockRate;
	/**
	 * 格挡率
	 */
	public int blockRate;
	/**
	 * 吸血几率
	 */
	public int suckHpRate;
	/**
	 * 吸血比例
	 */
	public int suckHpRatio;
	/**
	 * 反弹几率
	 */
	public int bounceRate;
	/**
	 * 反弹比例
	 */
	public int bounceRatio;

	/**
	 * 火焰伤害
	 */
	public int fireDamage;
	/**
	 * 寒冰伤害
	 */
	public int iceDamage;
	/**
	 * 雷电伤害
	 */
	public int thunderDamage;
	/**
	 * 毒素伤害
	 */
	public int poisonDamage;
	/**
	 * 神圣伤害
	 */
	public int holyDamage;
	/**
	 * 暗影伤害
	 */
	public int shadowDamage;
	/**
	 * 火焰抗性
	 */
	public int fireDef;
	/**
	 * 寒冰抗性
	 */
	public int iceDef;
	/**
	 * 雷电抗性
	 */
	public int thunderDef;
	/**
	 * 毒素抗性
	 */
	public int poisonDef;
	/**
	 * 神圣抗性
	 */
	public int holyDef;
	/**
	 * 暗影抗性
	 */
	public int shadowDef;
	
	/**
	 * 伤害加成
	 */
	public int damageIncrease;
	/**
	 * 伤害减免
	 */
	public int damageDecrease;


	/**
	 * 眩晕值
	 */
	public int dizziness;
	/**
	 * 减速值
	 */
	public int slow;
	/**
	 * 冻结值
	 */
	public int freeze;
	/**
	 * 沉默值
	 */
	public int silence;
	/**
	 * 恐惧值
	 */
	public int fear;
	/**
	 * 中毒值
	 */
	public int poisoning;
	/**
	 * 抗眩晕
	 */
	public int dizzinessDef;
	/**
	 * 抗减速
	 */
	public int slowDef;
	/**
	 * 抗冻结
	 */
	public int freezeDef;
	/**
	 * 抗沉默
	 */
	public int silenceDef;
	/**
	 * 抗恐惧
	 */
	public int fearDef;
	/**
	 * 抗中毒
	 */
	public int poisoningDef;
	/**
	 * 输出系数
	 */
	public int damageFactor1;
	/**
	 * 受伤系数
	 */
	public int damageFactor2;
}
