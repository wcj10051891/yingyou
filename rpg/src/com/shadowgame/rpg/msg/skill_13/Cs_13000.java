package com.shadowgame.rpg.msg.skill_13;

import com.shadowgame.rpg.modules.map.World;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;
import com.shadowgame.rpg.service.Services;

/**
 * 使用技能
 * @author wcj10051891@gmail.com
 * @date 2015年7月1日 下午12:02:07
 */
public class Cs_13000 extends ClientMsg {
	/**
	 * 技能id
	 */
	public int skillId;
	
	@Override
	public void handle(Player player) {
//		player.getPosition().getMapRegion().broadcast(new Sc_13000().from(player, skillId));
		Services.tcpService.broadcast(new Sc_13000().from(player, skillId), World.GROUP_NAME);
	}

}
