package com.shadowgame.rpg.ai.fsm;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseFSM {
	private BaseState currentState;
	private BaseState defaultState;
	private boolean running;
	protected Map<Class<? extends BaseState>, BaseState> states = new HashMap<Class<? extends BaseState>, BaseState>(5);

	public BaseFSM(BaseState defaultState, BaseState... otherStates) {
		this.defaultState = defaultState;
		this.defaultState.fsm = this;
		states.put(this.defaultState.getClass(), this.defaultState);
		for (BaseState state : otherStates) {
			states.put(state.getClass(), state);
			state.fsm = this;
		}
	}
	
	public void start() {
		changeState(defaultState.getClass());
		running = true;
	}
	
	public void changeState(Class<? extends BaseState> newStateType) {
		BaseState newState = this.states.get(newStateType);
		if(currentState != newState) {
			if(currentState != null)
				currentState.onExit();
			
			currentState = newState;
			currentState.onEnter();
			currentState.execute();
		}
	}

	public boolean isRunning() {
		return running;
	}
	
	public void stop() {
		running = false;
		if(currentState != null)
			currentState.onExit();
	}
}
