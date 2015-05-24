package com.shadowgame.rpg.ai.behaivortree;

public class Or extends ComposeNode {
	@Override
	public boolean execute() {
		for (Node childNode : nodes) {
			if (childNode.execute())
				return true;
		}
		return false;
	}
}