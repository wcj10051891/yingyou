package com.shadowgame.rpg.modules.event;

import xgame.core.event.EventListener;


public class PlayerEventListener extends EventListener<PlayerActionEvent> {
	
	@Override
	public Class<PlayerActionEvent> getEventType() {
		return PlayerActionEvent.class;
	}

	@Override
	public void onEvent(PlayerActionEvent event) {
		System.out.println("event fired:" + event);
	}


}