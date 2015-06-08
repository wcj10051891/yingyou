package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.intent.MoveToHomeIntent;
import com.shadowgame.rpg.ai.state.AIState;

public class MovingToHomeStateHandler implements StateHandler {

	@Override
	public void handleState(AbstractAI ai) {
		// ai.clearDesires();
		// Npc npc = (Npc) ai.getOwner();
		// npc.setTarget(null);
		// PacketSendUtility.broadcastPacket(npc, new SM_LOOKATOBJECT(npc));
		// npc.getAggroList().clear();
		// PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, 30, 0,
		// 0));
		// PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, 20, 0,
		// 0));
		// ai.addDesire(new MoveToHomeDesire(npc,
		// AIState.MOVINGTOHOME.getPriority()));
		//
		// ai.schedule();
		
		ai.clearIntents();
		ai.addIntent(new MoveToHomeIntent(AIState.MOVINGTOHOME.getPriority()));
		ai.start();
	}
}
