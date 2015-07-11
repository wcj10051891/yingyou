package com.shadowgame.rpg.msg.skill_13;

import java.util.List;

import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;

/**
 * 击中目标
 * @author wcj10051891@gmail.com
 * @date 2015年7月1日 下午12:02:07
 */
public class Cs_13001 extends ClientMsg {
	/**
	 * 技能id
	 */
	public int skillId;
	/**
	 * 目标id
	 */
	public List<Long> targetId;
	
	@Override
	public void handle(Player player) {
//		player.skillList.useSkill(skillId, targetId);
	}

}
