package com.shadowgame.rpg.modules.core;


/**
 * 可参战单位
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractFighter extends AbstractSpirit {
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
	 * 命中
	 */
	public int hit;
	/**
	 * 闪避
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
	 * 火焰伤害
	 */
	public int fireDef;
	/**
	 * 寒冰伤害
	 */
	public int iceDef;
	/**
	 * 雷电伤害
	 */
	public int thunderDef;
	/**
	 * 毒素伤害
	 */
	public int poisonDef;
	/**
	 * 神圣伤害
	 */
	public int holyDef;
	/**
	 * 暗影伤害
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
}
