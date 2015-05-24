package com.shadowgame.rpg.ai.fsm;

public interface State {
	public BaseFSM getFsm();
	public void onEnter();
	public void execute();
	public void onExit();
}
