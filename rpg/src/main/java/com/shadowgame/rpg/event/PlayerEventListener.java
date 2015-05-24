package com.shadowgame.rpg.event;

import xgame.core.event.EventListener;


public class PlayerEventListener extends EventListener<PlayerActionEvent> {

	@Override
	public void onEvent(PlayerActionEvent event) {
		System.out.println("event fired:" + event);
	}


}