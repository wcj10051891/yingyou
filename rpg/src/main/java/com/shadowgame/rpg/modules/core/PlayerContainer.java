package com.shadowgame.rpg.modules.core;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerContainer {
	private ConcurrentHashMap<Long, Player> id2Player = new ConcurrentHashMap<Long, Player>();
	private ConcurrentHashMap<String, Player> nickname2Player = new ConcurrentHashMap<String, Player>();
	private ConcurrentHashMap<String, Player> username2Player = new ConcurrentHashMap<String, Player>();
	
	public boolean isLogin(Long playerId) {
		return id2Player.containsKey(playerId);
	}
	public boolean isLogin(Player player) {
		return player == null || !id2Player.contains(player.getObjectId());
	}
	
	public void add(Player player) {
		id2Player.put(player.getObjectId(), player);
		nickname2Player.put(player.entity.nickname, player);
		username2Player.put(player.entity.username, player);
	}
	
	public void remove(Player player) {
		id2Player.remove(player.getObjectId());
		nickname2Player.remove(player.entity.nickname);
		username2Player.remove(player.entity.username);
	}
	
	public Player getOnlinePlayer(Long playerId) {
		return id2Player.get(playerId);
	}
	
	public Player getOnlinePlayer(String nickname) {
		return nickname2Player.get(nickname);
	}
	
	public Player getOnlinePlayerByUser(String username) {
		return username2Player.get(username);
	}
	
	public Collection<Player> getOnlinePlayers() {
		return id2Player.values();
	}
}
