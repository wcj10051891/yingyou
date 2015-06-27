package com.shadowgame.rpg.msg.map_12;

import java.util.Arrays;
import java.util.List;

import com.shadowgame.rpg.modules.core.Monster;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 看不见的地图上的对象
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午5:22:56
 */
public class Sc_12002 extends Message {
	/**
	 * 看不见的monster
	 */
	public List<MonsterInfo> monster;
	/**
	 * 看不见的player
	 */
	public List<PlayerInfo> player;
	
	public Sc_12002 from(Player player) {
		this.player = Arrays.asList(new PlayerInfo().from(player));
		return this;
	}
	
	public Sc_12002 from(Monster monster) {
		this.monster = Arrays.asList(new MonsterInfo().from(monster));
		return this;
	}
}
