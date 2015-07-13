package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.action.MoveToHomeAction;
import com.shadowgame.rpg.ai.state.AIState;

public class MovingToHomeStateHandler implements StateHandler {

	@Override
	public void handleState(AbstractAI ai) {
		ai.clearActions();
		ai.addAction(new MoveToHomeAction(AIState.MOVINGTOHOME.getPriority()));
	}
}
