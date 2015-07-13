package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.modules.monster.Monster;

public class StopStateHandler implements StateHandler {

	@Override
	public void handleState(AbstractAI ai) {
		ai.clearActions();
		((Monster) ai.getOwner()).getHateList().clear();
		ai.stop();
	}

}
