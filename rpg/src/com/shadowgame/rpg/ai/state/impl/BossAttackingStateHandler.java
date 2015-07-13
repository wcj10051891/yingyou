package com.shadowgame.rpg.ai.state.impl;

import java.util.Collection;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.action.AttackAction;
import com.shadowgame.rpg.ai.action.MoveToTargetAction;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.util.MapUtil;

public class BossAttackingStateHandler implements StateHandler {

	/**
	 * 攻击状态逻辑
	 */
	@Override
	public void handleState(AbstractAI ai) {
		 ai.clearActions();
		
		 Monster owner = (Monster)ai.getOwner();
		 AbstractFighter target = ai.getOwner().getHateList().getMostHate();
		 if(target == null) {
			 Collection<Player> players = ai.getOwner().getVisibilityObjectsByType(Player.class);
			 if(players == null || players.isEmpty())
				 return;
			 double distance = 0;
			 for (Player player : players) {
				 double d = MapUtil.calcDistance(owner, player);
				 if(distance == 0 || d < distance) {
					 target = player;
					 distance = d;
				 }
			 }
		 }
		 if(target == null)
			 return;
		 ai.addAction(new AttackAction(target, AIState.ATTACKING.getPriority()));
		 ai.addAction(new MoveToTargetAction(target, 1));
	}
}
