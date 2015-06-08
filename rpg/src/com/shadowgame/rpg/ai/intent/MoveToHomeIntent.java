package com.shadowgame.rpg.ai.intent;

import com.shadowgame.rpg.ai.AbstractAI;

public class MoveToHomeIntent extends Intent {

	public MoveToHomeIntent(int power) {
		super(power);
	}

	@Override
	public boolean execute(AbstractAI ai) {
		System.out.println("move to home intent");
		return false;
	}
}
