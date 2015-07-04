package com.shadowgame.rpg.modules.skill;

import xgame.core.util.RandomUtils;

import com.shadowgame.rpg.core.Constants;
import com.shadowgame.rpg.modules.core.AbstractFighter;

public abstract class SkillUtils {
	/**
	 * 直接伤害计算，返回[最终伤害值，是否暴击，是否格挡，吸血量，反弹伤害量]
	 * @param attacker
	 * @param skill
	 * @param target
	 * @return
	 */
	public static Object[] attack(AbstractFighter attacker, FighterSkill skill, AbstractFighter target) {
		//计算命中率
		int hitRate = (int)(1 + Math.max(Math.min(attacker.hit - target.dodge, 0), -0.6 * Constants.max));
		hitRate *= Constants.max;
		if(hitRate < RandomUtils.nextRandomInt(1, Constants.max)) {
			//计算暴击率
			int damage = Math.max(attacker.atk - target.def, 0) * Constants.damagePercent;
			int fireDamage = Math.max(attacker.fireDamage - target.fireDef, 0) * Constants.fireDamagePercent;
			int iceDamage = Math.max(attacker.iceDamage - target.iceDef, 0) * Constants.iceDamagePercent;
			int thunderDamage = Math.max(attacker.thunderDamage - target.thunderDef, 0) * Constants.thunderDamagePercent;
			int poisonDamage = Math.max(attacker.poisonDamage - target.poisonDef, 0) * Constants.poisonDamagePercent;
			int holyDamage = Math.max(attacker.holyDamage - target.holyDef, 0) * Constants.holyDamagePercent;
			int shadowDamage = Math.max(attacker.shadowDamage - target.shadowDef, 0) * Constants.shadowDamagePercent;
			int total = damage + fireDamage + iceDamage + thunderDamage + poisonDamage + holyDamage + shadowDamage + Constants.damageAdd;
			int critRate = Math.min(Math.max(attacker.critRate - target.critDefRate, 0), Constants.max);
			boolean isCrit = false;
			if(critRate < RandomUtils.nextRandomInt(1, Constants.max)) {
				isCrit = true;
				//暴击
				total *= 2 + (Math.max(attacker.crit + target.critDef, -0.5 * Constants.max));
			}
			boolean isBlock = false;
			int blockRate = Math.min(Math.max(attacker.blockRate - target.breakBlockRate, 0), Constants.max);
			if(blockRate < RandomUtils.nextRandomInt(1, Constants.max)) {
				isBlock = true;
				//格挡
				total = (int)(total * 0.4d);
			}
			//属性加成伤害
			int add = Math.max(1 + attacker.damageIncrease - target.damageDecrease, 0);
			total *= add;
			//buff属性
			//
			//伤害修正
			total = total * attacker.damageFactor1 * target.damageFactor2;
			//是否吸血
			int suckHp = 0;
			//是否反弹
			int rebound = 0;
			return new Object[]{total, isCrit, isBlock, suckHp, rebound};
		}
		return null;
	}
	
	/**
	 * 持续伤害计算
	 * @param attacker
	 * @param skill
	 * @param target
	 * @return
	 */
	public static Object[] contAttack(AbstractFighter attacker, FighterSkill skill, AbstractFighter target) {
		//计算命中率
		int hitRate = (int)(1 + Math.max(Math.min(attacker.hit - target.dodge, 0), -0.6 * Constants.max));
		hitRate *= Constants.max;
		if(hitRate < RandomUtils.nextRandomInt(1, Constants.max)) {
			//基础伤害
			int total = Math.max(attacker.atk - target.def, 0);
			//属性加成伤害
			total = total * Math.max(1 + attacker.damageIncrease - target.damageDecrease, 0);
			//buff&debuff伤害

			//伤害修正
			total = total * attacker.damageFactor1 * target.damageFactor2;
			//基于生命上限上限+固定伤害
			return new Object[]{total};
		}
		return null;
	}
}
