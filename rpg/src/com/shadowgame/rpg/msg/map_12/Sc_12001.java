package com.shadowgame.rpg.msg.map_12;

import java.util.Arrays;
import java.util.List;

import com.shadowgame.rpg.modules.core.Monster;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 新看见的地图上的对象
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午4:59:11
 */
public class Sc_12001 extends Message {
	/**
	 * 新看见的monster
	 */
	public List<MonsterInfo> monster;
	/**
	 * 新看见的player
	 */
	public List<PlayerInfo> player;
	
	public Sc_12001 from(Player player) {
		this.player = Arrays.asList(new PlayerInfo().from(player));
		return this;
	}
	
	public Sc_12001 from(Monster monster) {
		this.monster = Arrays.asList(new MonsterInfo().from(monster));
		return this;
	}
}
