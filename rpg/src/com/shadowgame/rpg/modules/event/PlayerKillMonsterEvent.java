package com.shadowgame.rpg.modules.event;

import com.shadowgame.rpg.modules.core.Player;

public class PlayerKillMonsterEvent extends PlayerEvent {

	public int monsterId;
	
	public PlayerKillMonsterEvent(Player player, int monsterId) {
		super(player);
		this.monsterId = monsterId;
	}

}
