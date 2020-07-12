package com.shadowgame.rpg.modules.event;

import com.shadowgame.rpg.modules.player.Player;

public class PlayerKillMonsterEvent extends PlayerEvent {

	public int monsterId;
	
	public PlayerKillMonsterEvent(Player player, int monsterId) {
		super(player);
		this.monsterId = monsterId;
	}

}
