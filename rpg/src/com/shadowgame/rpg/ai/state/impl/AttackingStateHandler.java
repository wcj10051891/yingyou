package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.core.Npc;

public class AttackingStateHandler implements StateHandler {

	/**
	 * State ATTACKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AbstractAI ai) {
//		 ai.clearIntents();
//		
//		 Creature target = ((Npc)ai.getOwner()).getAggroList().getMostHated();
//		 if(target == null)
//		 return;
//		
//		 Npc owner = (Npc) ai.getOwner();
//		 owner.setTarget(target);
//		
//		 owner.getMoveController().setSpeed(owner.getGameStats().getCurrentStat(StatEnum.SPEED)
//		 / 1000f);
//		 owner.getMoveController().setDistance(owner.getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE)
//		 / 1000f);
//		
//		 if(owner.getNpcSkillList() != null)
//		 ai.addDesire(new SkillUseDesire(owner,
//		 AIState.USESKILL.getPriority()));
//		 ai.addDesire(new AttackDesire(owner, target,
//		 AIState.ATTACKING.getPriority()));
//		 ai.addDesire(new MoveToTargetDesire(owner, target,
//		 AIState.ATTACKING.getPriority()));
//		
//		 ai.start();
	}
}
