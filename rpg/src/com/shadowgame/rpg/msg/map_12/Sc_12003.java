package com.shadowgame.rpg.msg.map_12;

import java.util.Arrays;
import java.util.List;

import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 看不见的地图上的对象
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午5:22:56
 */
public class Sc_12003 extends Message {
	/**
	 * 看不见的地图对象id，player，monster，npc等
	 */
	public List<Integer> ids;
	
	public Sc_12003 from(MapObject object) {
		this.ids = Arrays.asList(object.getObjectId());
		return this;
	}
}
