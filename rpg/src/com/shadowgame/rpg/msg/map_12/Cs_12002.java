package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;

/**
 * 请求获取附近的信息
 * @author wcj10051891@gmail.com
 * @date 2015年7月10日 上午10:45:56
 */
public class Cs_12002 extends ClientMsg {

	@Override
	public void handle(Player player) {
		player.send(new Sc_12002().from(player.getVisibilityObjects()));
	}

}
