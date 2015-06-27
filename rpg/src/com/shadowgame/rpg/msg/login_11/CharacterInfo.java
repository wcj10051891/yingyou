package com.shadowgame.rpg.msg.login_11;

import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.persist.entity.TPlayer;

/**
 * 角色信息
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 上午11:49:36
 */
public class CharacterInfo extends Message {
	/**
	 * playerId
	 */
	public long playerId;
	/**
	 * 等级
	 */
	public int lv;
	/**
	 * 昵称
	 */
	public String nickname;
	/**
	 * 职业
	 */
	public byte vocation;
	
	public CharacterInfo from(TPlayer entity) {
		this.playerId = entity.id;
		this.lv = entity.lv;
		this.nickname = entity.nickname;
		this.vocation = entity.vocation.byteValue();
		return this;
	}
}
