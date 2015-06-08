package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;

public class ActiveNpcStateHandler implements StateHandler {
	/**
	 * State ACTIVE AI NpcAi
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
		// if(ai.desireQueueSize() == 0)
		// ai.handleEvent(AIEvent.NOTHING_TODO);
		// else
		// ai.schedule();
	}
}
