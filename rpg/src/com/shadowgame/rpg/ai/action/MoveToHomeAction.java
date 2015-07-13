package com.shadowgame.rpg.ai.action;

import java.util.LinkedList;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.modules.map.PathFinding;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.monster.Monster;

public class MoveToHomeAction extends AIAction {
	
	public MoveToHomeAction(int power) {
		super(power);
	}

	@Override
	public boolean execute(AbstractAI ai) {
		Monster monster = (Monster)ai.getOwner();
		monster.setTarget(null);
		if(monster.isAtBornPoint()) {
			ai.setAiState(AIState.THINKING);
			return false;
		}
		Position pos = monster.getPosition();
		Point home = new Point(monster.entity.bornX, monster.entity.bornY);
		if(!monster.getMoveManager().isMoveToDest(home)) {
			LinkedList<Point> path = PathFinding.findByLine(monster.getGameMap(), pos.getPoint(), home);
			monster.getMoveManager().start(home, path);
		}
		return true;
	}
}
