package com.shadowgame.rpg.msg.skill_13;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 被攻击者中招信息
 * @author wcj10051891@gmail.com
 * @date 2015年7月9日 下午7:49:03
 */
public class AttackInfo extends Message {
	/**
	 * 被攻击者
	 */
	public long targetId;
	/**
	 * 类型
	 */
	public byte type;
	/**
	 * 伤害值
	 */
	public int damage;
}
