package com.shadowgame.rpg.modules.event;

import com.shadowgame.rpg.modules.player.Player;

public class PlayerActionEvent extends PlayerEvent {

	public String action;
	public PlayerActionEvent(Player player, String action) {
		super(player);
		this.action = action;
	}

}
