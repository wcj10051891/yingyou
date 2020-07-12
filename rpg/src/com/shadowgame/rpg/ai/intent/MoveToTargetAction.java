package com.shadowgame.rpg.ai.intent;

import java.util.LinkedList;

import com.shadowgame.rpg.ai.AbstractAI;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.map.PathFinding;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.modules.map.Position;

public class MoveToTargetAction extends AIAction {

	private AbstractFighter owner;
	private AbstractFighter target;

	public MoveToTargetAction(AbstractFighter owner, AbstractFighter target, int power) {
		super(power);
		this.owner = owner;
		this.target = target;
	}

	@Override
	public boolean execute(AbstractAI ai) {
		if (owner == null || owner.isDied())
			return false;
		if (target == null || target.isDied())
			return false;

		Position current = owner.getPosition();
		LinkedList<Point> path = PathFinding.find(owner.getGameMap(),
				current.getPoint(), target.getPosition().getPoint());
		owner.getMoveManager().start(path);

		return true;
	}
}
