package com.shadowgame.rpg.ai.event.handler;

import java.util.Collection;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.core.Player;

public class NotSeePlayerEventHandler implements EventHandler {
	@Override
	public void handleEvent(AbstractAI ai) {
		Collection<Player> ps = ai.getOwner().getPosition().getMapRegion().getVisibilityObjectsByType(Player.class);
		if(ps.isEmpty())
			ai.setAiState(AIState.THINKING);
	}
}
