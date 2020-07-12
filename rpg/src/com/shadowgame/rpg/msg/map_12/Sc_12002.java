package com.shadowgame.rpg.msg.map_12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.shadowgame.rpg.modules.map.MapObject;
import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 新看见的地图上的对象
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午4:59:11
 */
public class Sc_12002 extends Message {
	/**
	 * 新看见的monster
	 */
	public List<MonsterInfo> monster;
	/**
	 * 新看见的player
	 */
	public List<PlayerInfo> player;
	
	public Sc_12002 from(Collection<MapObject> objects) {
		this.player = new ArrayList<PlayerInfo>();
		this.monster = new ArrayList<MonsterInfo>();
		for (MapObject o : objects) {
			if(o instanceof Monster)
				this.monster.add(new MonsterInfo().from((Monster)o));
			else if(o instanceof Player)
				this.player.add(new PlayerInfo().from((Player)o));
		}
		return this;
	}
}
