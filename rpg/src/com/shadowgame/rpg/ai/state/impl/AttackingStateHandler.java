package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.action.AttackAction;
import com.shadowgame.rpg.ai.action.MoveToTargetAction;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;

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
		
		 ai.addAction(new AttackAction(target, AIState.ATTACKING.getPriority()));
		 ai.addAction(new MoveToTargetAction(target, 1));
	}
}
