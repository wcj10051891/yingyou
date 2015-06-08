package com.shadowgame.rpg.ai.state;


public enum AIState {
	THINKING(5),
	TALKING(4),
	AGGRO(3),
	ACTIVE(3),
	USESKILL(3),
	ATTACKING(2),
	RESTING(1),
	MOVINGTOHOME(1),
	NONE(0);

	private int priority;

	private AIState(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
}
