package com.shadowgame.rpg.modules.skill;

import xgame.core.util.RandomUtils;

import com.shadowgame.rpg.core.Constants;
import com.shadowgame.rpg.modules.buff.BuffTask;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.persist.entity.TBuff;

public abstract class SkillUtils {
	
	/**
	 * 计算是否命中
	 * @param attacker
	 * @param skill
	 * @param target
	 * @return
	 */
	public static boolean isHit(AbstractFighter attacker, FighterSkill skill, AbstractFighter target) {
		//计算命中率
		float hitRate = 1 + Math.max(Math.min((attacker.hit  - target.dodge) / Constants.wanFloat, 0), -0.6f);
		hitRate *= Constants.wanInt;
		return hitRate < RandomUtils.nextRandomInt(1, Constants.wanInt);
	}
	
	/**
	 * 直接伤害计算，返回[最终伤害值，是否暴击，是否格挡，吸血量，反弹伤害量]
	 * @param attacker
	 * @param skill
	 * @param target
	 * @return
	 */
	public static Object[] attack(AbstractFighter attacker, FighterSkill skill, AbstractFighter target) {
		if(isHit(attacker, skill, target)) {
			//中转普通伤害
			float damage = Math.max(attacker.atk - target.def, 0) * Constants.damagePercent / Constants.baiFloat;
			//中转火焰伤害
			float fireDamage = Math.max(attacker.fireDamage - target.fireDef, 0) * Constants.fireDamagePercent / Constants.baiFloat;
			//中转寒冰伤害
			float iceDamage = Math.max(attacker.iceDamage - target.iceDef, 0) * Constants.iceDamagePercent / Constants.baiFloat;
			//中转雷电伤害
			float thunderDamage = Math.max(attacker.thunderDamage - target.thunderDef, 0) * Constants.thunderDamagePercent / Constants.baiFloat;
			//中转毒素伤害
			float poisonDamage = Math.max(attacker.poisonDamage - target.poisonDef, 0) * Constants.poisonDamagePercent / Constants.baiFloat;
			//中转神圣伤害
			float holyDamage = Math.max(attacker.holyDamage - target.holyDef, 0) * Constants.holyDamagePercent / Constants.baiFloat;
			//中转暗影伤害
			float shadowDamage = Math.max(attacker.shadowDamage - target.shadowDef, 0) * Constants.shadowDamagePercent / Constants.baiFloat;
			//基础伤害
			float total = damage + fireDamage + iceDamage + thunderDamage + poisonDamage + holyDamage + shadowDamage + Constants.damageAdd;
			//暴击率
			int critRate = (int)(Math.min(Math.max((attacker.critRate - target.critDefRate) / Constants.wanFloat, 0), 1) * Constants.wanFloat);
			boolean isCrit = false;
			if(critRate < RandomUtils.nextRandomInt(1, Constants.wanInt)) {
				isCrit = true;
				//暴击
				total = total * (2 + (Math.max((attacker.crit + target.critDef) / Constants.wanFloat, -0.5f)));
			}
			boolean isBlock = false;
			//格挡率
			int blockRate = (int)(Math.min(Math.max((attacker.blockRate - target.breakBlockRate) / Constants.wanFloat, 0), 1) * Constants.wanFloat);
			if(blockRate < RandomUtils.nextRandomInt(1, Constants.baiInt)) {
				isBlock = true;
				//格挡
				total = total * 0.4f;
			}
			//属性加成伤害
			float add = Math.max(1 + (attacker.damageIncrease - target.damageDecrease) / Constants.wanFloat, 0);
			total = total * add;
			//buff属性
			total = total * buffRate(attacker, target);
			//伤害修正
			total = (int)(total * (attacker.damageFactor1 / Constants.wanFloat) * (target.damageFactor2 * Constants.wanFloat));
			//是否吸血
			int suckHp = 0;
			if(attacker.suckHpRate > 0) {
				if(attacker.suckHpRate < RandomUtils.nextRandomInt(1, Constants.wanInt)) {
					suckHp = (int)(total * attacker.suckHpRatio / Constants.wanFloat);
				}
			}
			//是否反弹
			int rebound = 0;
			if(attacker.reboundRate > 0) {
				if(attacker.reboundRate < RandomUtils.nextRandomInt(1, Constants.wanInt)) {
					rebound = (int)(total * attacker.reboundRatio / Constants.wanFloat);
				}
			}
			return new Object[]{total, isCrit, isBlock, suckHp, rebound};
		}
		return null;
	}
	
	private static float buffRate(AbstractFighter attacker, AbstractFighter target) {
		int inc = 0;
		int dec = 1;
		for (BuffTask buff : attacker.buffList.getBuffs()) {
			TBuff entity = buff.buffLogic.getBuff();
			if(entity.damageIncrease > 0)
				inc += entity.damageIncrease;
			if(entity.damageDecrease > 0)
				dec = (int)(dec * (1 - entity.damageDecrease / Constants.baiFloat));
		}
		for (BuffTask buff : target.buffList.getBuffs()) {
			TBuff entity = buff.buffLogic.getBuff();
			if(entity.hitDamageIncrease > 0)
				inc += entity.hitDamageIncrease;
			if(entity.hitDamageDecrease > 0)
				dec = (int)(dec * (1 - entity.hitDamageDecrease/ Constants.baiFloat));
		}
		dec = 1 - dec;
		return 1 + inc / Constants.baiFloat - dec;
	}
	
	/**
	 * 持续伤害计算
	 * @param attacker
	 * @param skill
	 * @param target
	 * @return
	 */
	public static int contAttack(BuffTask buff) {
		TBuff entity = buff.buffLogic.getBuff();
		//基础伤害
		float total = Math.max(buff.source.atk - buff.target.def, 0) * entity.normalDamage / Constants.baiFloat;
		//属性加成伤害
		total = total * (int)Math.max(1 + (buff.source.damageIncrease - buff.target.damageDecrease) / Constants.wanFloat, 0);
		//buff&debuff伤害
		total = total * buffRate(buff.source, buff.target);
		//伤害修正
		total = total * (buff.source.damageFactor1 / Constants.wanFloat) * (buff.target.damageFactor2 * Constants.wanFloat);
		//基于攻防属性伤害+基于生命上限上限+固定伤害
		total = total + buff.target.maxhp * entity.hpMaxDamage / Constants.baiFloat + entity.fixDamage;
		return (int)total;
	}
}
