package com.shadowgame.rpg.msg.skill_13;

import java.util.List;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 伤害信息广播
 * @author wcj10051891@gmail.com
 * @date 2015年7月9日 下午7:46:46
 */
public class Sc_13001 extends Message {
	/**
	 * 攻击者
	 */
	public long attackerId;
	/**
	 * 被攻击者中招信息
	 */
	public List<AttackInfo> attackInfos; 
}
