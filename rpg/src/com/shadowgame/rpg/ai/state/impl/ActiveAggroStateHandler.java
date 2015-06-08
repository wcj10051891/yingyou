package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;

public class ActiveAggroStateHandler implements StateHandler {
	/**
	 * State ACTIVE AI AggressiveMonsterAi AI GuardAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
		// ai.clearDesires();
		// Npc owner = (Npc) ai.getOwner();
		// if (owner.hasWalkRoutes())
		// {
		// ai.addDesire(new WalkDesire(owner, AIState.ACTIVE.getPriority()));
		// }
		//
		// //if there are players visible - add AggressionDesire filter
		// int playerCount = 0;
		// for(VisibleObject visibleObject : owner.getKnownList())
		// {
		// if (visibleObject instanceof Player)
		// {
		// Race playerRace = ((Player) visibleObject).getCommonData().getRace();
		// int playerLevel = ((Player)
		// visibleObject).getCommonData().getLevel();
		//
		// if(owner.isAggressiveTo(playerRace)
		// && (owner.getLevel() + 10 > playerLevel))
		// playerCount++;
		// }
		// }
		// if(playerCount > 0)
		// {
		// ai.addDesire(new AggressionDesire(owner,
		// AIState.ACTIVE.getPriority()));
		// }
		//
		// if(ai.desireQueueSize() == 0)
		// ai.handleEvent(AIEvent.NOTHING_TODO);
		// else
		// ai.schedule();
	}
}
