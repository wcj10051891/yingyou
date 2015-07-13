package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.event.AIEvent;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.monster.Monster;

public class ThinkingStateHandler implements StateHandler {

	/**
	 * State THINKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
		ai.clearActions();

		Monster owner = (Monster) ai.getOwner();
		if (owner.getHateList().getMostHate() != null) {
			ai.setAiState(AIState.ATTACKING);
			return;
		}
		
		if (!owner.isAtBornPoint()) {
			ai.setAiState(AIState.MOVINGTOHOME);
			return;
		}
		ai.handleEvent(AIEvent.STOP_AI);
	}
}
