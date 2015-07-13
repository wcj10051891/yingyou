package com.shadowgame.rpg.ai.action;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.util.MapUtil;

public class AttackAction extends AIAction {

	private AbstractFighter target;

	public AttackAction(AbstractFighter target, int power) {
		super(power);
		this.target = target;
	}

	@Override
	public boolean execute(AbstractAI ai) {
		Monster owner = (Monster)ai.getOwner();
		if(target == null || target.isDie()) {
			owner.getHateList().removeHate(target);
			ai.setAiState(AIState.THINKING);
			return false;
		}
		Position targetPos = target.getPosition();
		if(MapUtil.calcDistance(owner, targetPos.getX(), targetPos.getY()) >= owner.entity.followDistance) {
			owner.getHateList().removeHate(target);
			ai.setAiState(AIState.THINKING);
			return false;
		}
		
		if(MapUtil.calcDistance(owner, owner.entity.bornX, owner.entity.bornY) >= owner.entity.bornDistance) {
			owner.getHateList().removeHate(target);
			ai.setAiState(AIState.THINKING);
			return false;
		}
		owner.getSkillList().getDefaultSkill().fire(target);
		return true;
	}
}
