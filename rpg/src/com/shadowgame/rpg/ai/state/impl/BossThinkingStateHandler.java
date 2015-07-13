package com.shadowgame.rpg.ai.state.impl;

import java.util.Collection;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.event.AIEvent;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.util.MapUtil;

public class BossThinkingStateHandler implements StateHandler {

	/**
	 * State THINKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
		ai.clearActions();

		Monster owner = (Monster) ai.getOwner();
		AbstractFighter target = owner.getHateList().getMostHate();
		if (target == null) {
			Collection<Player> players = owner.getVisibilityObjectsByType(Player.class);
			if (players == null || players.isEmpty())
				return;
			double distance = 0;
			for (Player player : players) {
				double d = MapUtil.calcDistance(owner, player);
				if (distance == 0 || d < distance) {
					target = player;
					distance = d;
				}
			}
		}
		if (target != null) {
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
