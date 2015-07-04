package com.shadowgame.rpg.msg.skill_13;

import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;

/**
 * 学习技能
 * @author wcj10051891@gmail.com
 * @date 2015年7月1日 下午12:02:07
 */
public class Cs_13001 extends ClientMsg {
	
	/**
	 * 技能id
	 */
	public int skillId;
	
	@Override
	public void handle(Player player) {
		player.skillList.learnSkill(skillId);
		player.skillList.useSkill(skillId);
	}

}
