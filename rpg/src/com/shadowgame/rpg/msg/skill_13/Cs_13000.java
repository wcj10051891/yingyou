package com.shadowgame.rpg.msg.skill_13;

import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.event.PlayerKillMonsterEvent;
import com.shadowgame.rpg.net.msg.ClientMsg;

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
	/**
	 * 目标id
	 */
	public int targetId;
	
	@Override
	public void handle(Player player) {
//		player.skillList.useSkill(skillId, targetId);
//		player.missionManager.accept(1);
		player.eventDispatcher.fireEvent(new PlayerKillMonsterEvent(player, 1));
//		player.missionManager.giveUp(1);
		player.missionManager.finish(1);
	}

}
