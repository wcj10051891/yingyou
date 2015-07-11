package com.shadowgame.rpg.modules.skill;

import xgame.core.util.RandomUtils;

import com.shadowgame.rpg.core.Constants;
import com.shadowgame.rpg.modules.buff.BuffTask;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.core.AttrType;
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
		float hitRate = 1 + Math.max(Math.min((attacker.attrs.get(AttrType.hit)  - target.attrs.get(AttrType.dodge)) / Constants.wanFloat, 0), -0.6f);
		hitRate *= Constants.wanInt;
		return RandomUtils.nextRandomInt(1, Constants.wanInt) <= hitRate;
	}
	
	/**
	 * 直接伤害计算，返回[最终伤害值，是否暴击，是否格挡，吸血量，反弹伤害量]
	 * @param attacker
	 * @param skill
	 * @param target
	 * @return
	 */
	public static Object[] attack(AbstractFighter attacker, FighterSkill skill, AbstractFighter target) {
		//中转普通伤害
		float damage = Math.max(attacker.attrs.get(AttrType.atk) - target.attrs.get(AttrType.def), 0) * skill.entity.damagePercent / Constants.baiFloat;
		//中转火焰伤害
		float fireDamage = Math.max(attacker.attrs.get(AttrType.fireDamage) - target.attrs.get(AttrType.fireDef), 0) * Constants.fireDamagePercent / Constants.baiFloat;
		//中转寒冰伤害
		float iceDamage = Math.max(attacker.attrs.get(AttrType.iceDamage) - target.attrs.get(AttrType.iceDef), 0) * Constants.iceDamagePercent / Constants.baiFloat;
		//中转雷电伤害
		float thunderDamage = Math.max(attacker.attrs.get(AttrType.thunderDamage) - target.attrs.get(AttrType.thunderDef), 0) * Constants.thunderDamagePercent / Constants.baiFloat;
		//中转毒素伤害
		float poisonDamage = Math.max(attacker.attrs.get(AttrType.poisonDamage) - target.attrs.get(AttrType.poisonDef), 0) * Constants.poisonDamagePercent / Constants.baiFloat;
		//中转神圣伤害
		float holyDamage = Math.max(attacker.attrs.get(AttrType.holyDamage) - target.attrs.get(AttrType.holyDef), 0) * Constants.holyDamagePercent / Constants.baiFloat;
		//中转暗影伤害
		float shadowDamage = Math.max(attacker.attrs.get(AttrType.shadowDamage) - target.attrs.get(AttrType.shadowDef), 0) * Constants.shadowDamagePercent / Constants.baiFloat;
		//基础伤害
		float total = damage + fireDamage + iceDamage + thunderDamage + poisonDamage + holyDamage + shadowDamage + Constants.damageAdd;
		//暴击率
		int critRate = (int)(Math.min(Math.max((attacker.attrs.get(AttrType.critRate) - target.attrs.get(AttrType.critDefRate)) / Constants.wanFloat, 0), 1) * Constants.wanFloat);
		boolean isCrit = false;
		if(RandomUtils.nextRandomInt(1, Constants.wanInt) <= critRate) {
			isCrit = true;
			//暴击
			total = total * (2 + (Math.max((attacker.attrs.get(AttrType.crit) + target.attrs.get(AttrType.critDef)) / Constants.wanFloat, -0.5f)));
		}
		boolean isBlock = false;
		//格挡率
		int blockRate = (int)(Math.min(Math.max((attacker.attrs.get(AttrType.blockRate) - target.attrs.get(AttrType.breakBlockRate)) / Constants.wanFloat, 0), 1) * Constants.wanFloat);
		if(RandomUtils.nextRandomInt(1, Constants.baiInt) <= blockRate) {
			isBlock = true;
			//格挡
			total = total * 0.4f;
		}
		//属性加成伤害
		float add = Math.max(1 + (attacker.attrs.get(AttrType.damageIncrease) - target.attrs.get(AttrType.damageDecrease)) / Constants.wanFloat, 0);
		total = total * add;
		//buff属性
		total = total * buffRate(attacker, target);
		//伤害修正
		total = (int)(total * (attacker.attrs.get(AttrType.damageFactor1) / Constants.wanFloat) * (target.attrs.get(AttrType.damageFactor2) * Constants.wanFloat));
		//是否吸血
		int suckHp = 0;
		if(attacker.attrs.get(AttrType.suckHpRate) > 0) {
			if(RandomUtils.nextRandomInt(1, Constants.wanInt) <= attacker.attrs.get(AttrType.suckHpRate)) {
				suckHp = (int)(total * attacker.attrs.get(AttrType.suckHpRatio) / Constants.wanFloat);
			}
		}
		//是否反弹
		int rebound = 0;
		if(attacker.attrs.get(AttrType.reboundRate) > 0) {
			if(RandomUtils.nextRandomInt(1, Constants.wanInt) <= attacker.attrs.get(AttrType.reboundRate)) {
				rebound = (int)(total * attacker.attrs.get(AttrType.reboundRatio) / Constants.wanFloat);
			}
		}
		return new Object[]{total, isCrit, isBlock, suckHp, rebound};
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
	 * @param fighterSkill
	 * @param target
	 * @return
	 */
	public static int contAttack(BuffTask buff) {
		TBuff entity = buff.buffLogic.getBuff();
		//基础伤害
		float total = Math.max(buff.source.attrs.get(AttrType.atk) - buff.target.attrs.get(AttrType.def), 0) * entity.normalDamage / Constants.baiFloat;
		//属性加成伤害
		total = total * (int)Math.max(1 + (buff.source.attrs.get(AttrType.damageIncrease) - buff.target.attrs.get(AttrType.damageDecrease)) / Constants.wanFloat, 0);
		//buff&debuff伤害
		total = total * buffRate(buff.source, buff.target);
		//伤害修正
		total = total * (buff.source.attrs.get(AttrType.damageFactor1) / Constants.wanFloat) * (buff.target.attrs.get(AttrType.damageFactor2) * Constants.wanFloat);
		//基于攻防属性伤害+基于生命上限上限+固定伤害
		total = total + buff.target.attrs.get(AttrType.maxHp) * entity.hpMaxDamage / Constants.baiFloat + entity.fixDamage;
		return (int)total;
	}
}
