package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;

/**
 * 请求进入地图，返回Sc_12004
 * @author wcj10051891@gmail.com
 * @date 2015年7月10日 上午10:45:56
 */
public class Cs_12001 extends ClientMsg {
	/**
	 * 地图id
	 */
	public int mapId;

	@Override
	public void handle(Player player) {
	}

}
