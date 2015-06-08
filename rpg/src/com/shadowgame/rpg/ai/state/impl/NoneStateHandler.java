package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;

public class NoneStateHandler implements StateHandler {

	/**
	 * State NONE AI MonsterAi AI GuardAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
		ai.clearIntents();
//		((Npc) ai.getOwner()).getAggroList().clear();
		ai.stop();
	}
}
