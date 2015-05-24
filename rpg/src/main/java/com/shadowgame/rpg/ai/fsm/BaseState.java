package com.shadowgame.rpg.ai.fsm;

public abstract class BaseState implements State {
	protected BaseFSM fsm;
	public abstract void onEnter();
	public abstract void execute();
	public abstract void onExit();
	@Override
	public BaseFSM getFsm() {
		return fsm;
	}
}
