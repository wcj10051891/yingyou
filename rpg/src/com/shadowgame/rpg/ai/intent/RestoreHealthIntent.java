package com.shadowgame.rpg.ai.intent;

import com.shadowgame.rpg.ai.AbstractAI;

public class RestoreHealthIntent extends Intent {

	public RestoreHealthIntent(int power) {
		super(power);
	}

	@Override
	public boolean execute(AbstractAI ai) {
		System.out.println("restore health intent");
		return false;
	}
}
