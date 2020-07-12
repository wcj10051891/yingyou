package com.shadowgame.rpg.ai.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.event.AIEvent;
import com.shadowgame.rpg.ai.event.handler.AttackedEventHandler;
import com.shadowgame.rpg.ai.event.handler.BackHomeEventHandler;
import com.shadowgame.rpg.ai.event.handler.NotSeePlayerEventHandler;
import com.shadowgame.rpg.ai.event.handler.NothingTodoEventHandler;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.ai.state.impl.AttackingStateHandler;
import com.shadowgame.rpg.modules.fight.AbstractFighter;

public class NormalMonsterAI extends AbstractAI {
	
	public NormalMonsterAI(AbstractFighter spirit) {
		super(spirit);
		//event
		this.addEventHandler(AIEvent.ATTACKED, new AttackedEventHandler());
		this.addEventHandler(AIEvent.BACK_HOME, new BackHomeEventHandler());
		this.addEventHandler(AIEvent.NOT_SEE_PLAYER, new NotSeePlayerEventHandler());
		this.addEventHandler(AIEvent.STOP, new NothingTodoEventHandler());
		//state
		this.addStateHandler(AIState.ATTACKING, new AttackingStateHandler());
//		this.addStateHandler(AIState.NONE, new NoneStateHandler());
	}
}
