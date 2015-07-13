package com.shadowgame.rpg.ai.action;

import java.util.LinkedList;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.map.PathFinding;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.monster.Monster;

public class MoveToTargetAction extends AIAction {

	private AbstractFighter target;

	public MoveToTargetAction(AbstractFighter target, int power) {
		super(power);
		this.target = target;
	}

	@Override
	public boolean execute(AbstractAI ai) {
		if (target == null || target.isDie()) {
			ai.setAiState(AIState.THINKING);
			return false;
		}
		Monster owner = (Monster)ai.getOwner();
		Point targetPoint = target.getPosition().getPoint();
		if(!owner.getMoveManager().isMoveToDest(targetPoint)) {
			Position current = owner.getPosition();
			LinkedList<Point> path = PathFinding.findByLine(owner.getGameMap(), current.getPoint(), targetPoint);
			owner.getMoveManager().start(targetPoint, path);
		}
		return true;
	}
}
