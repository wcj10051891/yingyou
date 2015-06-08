package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;

public class RestingStateHandler implements StateHandler {

	/**
	 * State RESTING AI NpcAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
		// ai.addDesire(new RestoreHealthDesire(ai.getOwner(),
		// AIState.RESTING.getPriority()));
	}
}
