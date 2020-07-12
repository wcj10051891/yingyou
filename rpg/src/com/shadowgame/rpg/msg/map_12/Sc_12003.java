package com.shadowgame.rpg.msg.map_12;

import java.util.ArrayList;
import java.util.List;

import com.shadowgame.rpg.modules.map.MapObject;
import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 看不见的地图上的对象
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午5:22:56
 */
public class Sc_12003 extends Message {
	/**
	 * 看不见的player
	 */
	public List<Long> player;
	/**
	 * 看不见monster
	 */
	public List<Long> monster;
	
	public Sc_12003 from(List<MapObject> objects) {
		this.player = new ArrayList<Long>();
		this.monster = new ArrayList<Long>();
		for (MapObject o : objects) {
			if(o instanceof Monster)
				this.monster.add(o.getObjectId());
			else if(o instanceof Player)
				this.player.add(o.getObjectId());
		}
		return this;
	}
}
