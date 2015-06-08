package com.shadowgame.rpg.ai.intent;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.event.AIEvent;

public class AttackIntent extends Intent {

	private int count;
	
	public AttackIntent(int power) {
		super(power);
	}

	@Override
	public boolean execute(AbstractAI ai) {
		System.out.println("attack intent：" + this.count);
		this.count++;
		if(this.count < 5)
			return true;
		
		ai.handleEvent(AIEvent.NOTHING_TODO);
		return false;
	}
}
