package com.shadowgame.rpg.ai.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.event.AIEvent;
import com.shadowgame.rpg.ai.event.handler.AttackedEventHandler;
import com.shadowgame.rpg.ai.event.handler.BackHomeEventHandler;
import com.shadowgame.rpg.ai.event.handler.NotSeePlayerEventHandler;
import com.shadowgame.rpg.ai.event.handler.NothingTodoEventHandler;
import com.shadowgame.rpg.ai.event.handler.SeePlayerEventHandler;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.ai.state.impl.AttackingStateHandler;
import com.shadowgame.rpg.ai.state.impl.NoneStateHandler;

public class NpcAI extends AbstractAI {
	public NpcAI() {
		//event
		this.addEventHandler(AIEvent.SEE_PLAYER, new SeePlayerEventHandler());
		this.addEventHandler(AIEvent.NOT_SEE_PLAYER, new NotSeePlayerEventHandler());
		this.addEventHandler(AIEvent.ATTACKED, new AttackedEventHandler());
		this.addEventHandler(AIEvent.BACK_HOME, new BackHomeEventHandler());
		this.addEventHandler(AIEvent.NOTHING_TODO, new NothingTodoEventHandler());
		//state
		this.addStateHandler(AIState.ATTACKING, new AttackingStateHandler());
		this.addStateHandler(AIState.NONE, new NoneStateHandler());
	}
	
	public static void main(String[] args) {
		NpcAI ai = new NpcAI();
		ai.handleEvent(AIEvent.SEE_PLAYER);
	}
}
