package com.shadowgame.rpg.event;

import com.shadowgame.rpg.modules.core.Player;

public class PlayerActionEvent extends PlayerEvent {

	public String action;
	public PlayerActionEvent(Player player, String action) {
		super(player);
		this.action = action;
	}

}
