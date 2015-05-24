package com.shadowgame.rpg.ai.behaivortree;

public class And extends ComposeNode {
	@Override
	public boolean execute() {
		for (Node childNode : nodes) {
			if(!childNode.execute())
				return false;
		}
		return true;
	}
}