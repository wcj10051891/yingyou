package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.intent.AttackIntent;
import com.shadowgame.rpg.ai.state.AIState;

public class AttackingStateHandler implements StateHandler {

	/**
	 * State ATTACKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
		// ai.clearDesires();
		//
		// Creature target = ((Npc)ai.getOwner()).getAggroList().getMostHated();
		// if(target == null)
		// return;
		//
		// Npc owner = (Npc) ai.getOwner();
		// owner.setTarget(target);
		// PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));
		//
		// owner.setState(CreatureState.WEAPON_EQUIPPED);
		// PacketSendUtility.broadcastPacket(owner,
		// new SM_EMOTION(owner, 30, 0, target.getObjectId()));
		// PacketSendUtility.broadcastPacket(owner,
		// new SM_EMOTION(owner, 19, 0, target.getObjectId()));
		//
		// owner.getMoveController().setSpeed(owner.getGameStats().getCurrentStat(StatEnum.SPEED)
		// / 1000f);
		// owner.getMoveController().setDistance(owner.getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE)
		// / 1000f);
		//
		// if(owner.getNpcSkillList() != null)
		// ai.addDesire(new SkillUseDesire(owner,
		// AIState.USESKILL.getPriority()));
		// ai.addDesire(new AttackDesire(owner, target,
		// AIState.ATTACKING.getPriority()));
		// ai.addDesire(new MoveToTargetDesire(owner, target,
		// AIState.ATTACKING.getPriority()));
		//
		// ai.schedule();
		
		ai.clearIntents();
		ai.addIntent(new AttackIntent(AIState.ATTACKING.getPriority()));
		ai.start();
	}
}
