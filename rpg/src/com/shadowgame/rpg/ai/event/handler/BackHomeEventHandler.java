package com.shadowgame.rpg.ai.event.handler;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;

public class BackHomeEventHandler implements EventHandler {
	@Override
	public void handleEvent(AbstractAI ai) {
		ai.setAiState(AIState.RESTING);
	}
}
