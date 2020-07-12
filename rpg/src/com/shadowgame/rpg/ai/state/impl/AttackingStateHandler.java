package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.intent.AttackAction;
import com.shadowgame.rpg.ai.intent.MoveToTargetAction;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.monster.Monster;

public class AttackingStateHandler implements StateHandler {

	/**
	 * 攻击状态逻辑
	 */
	@Override
	public void handleState(AbstractAI ai) {
		 ai.clearActions();
		
		 AbstractFighter target = ai.getOwner().getHateList().getMostHate();
		 if(target == null)
			 return;
		
		 Monster owner = (Monster) ai.getOwner();
		 owner.setTarget(target);
		
		 ai.addAction(new AttackAction(owner, target, AIState.ATTACKING.getPriority()));
		 ai.addAction(new MoveToTargetAction(owner, target, 1));
		
		 ai.start();
	}
}
