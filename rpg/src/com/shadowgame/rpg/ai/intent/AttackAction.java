package com.shadowgame.rpg.ai.intent;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;

public class AttackAction extends AIAction {

	private AbstractFighter owner;
	private AbstractFighter target;

	public AttackAction(AbstractFighter owner, AbstractFighter target, int power) {
		super(power);
		this.owner = owner;
		this.target = target;
	}

	@Override
	public boolean execute(AbstractAI ai) {
		if(target == null || target.isDied()) {
			owner.getHateList().removeHate(target);
			ai.setAiState(AIState.THINKING);
			return false;
		}
		//检测与目标的距离是否超出限制，超出了则进入thinking状态
		//
		owner.getSkillList().getDefaultSkill().fire(target);
		
		
		return true;
	}
}
