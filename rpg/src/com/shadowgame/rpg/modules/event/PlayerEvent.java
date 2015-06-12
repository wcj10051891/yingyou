package com.shadowgame.rpg.modules.event;

import xgame.core.event.Event;

import com.shadowgame.rpg.modules.core.Player;

public class PlayerEvent extends Event {
	public Player player;

	public PlayerEvent(Player player) {
		this.player = player;
	}
}
