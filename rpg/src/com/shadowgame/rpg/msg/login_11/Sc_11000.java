package com.shadowgame.rpg.msg.login_11;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 登录协议响应
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午5:02:48
 */
public class Sc_11000 extends Message {
	/**
	 * 角色列表
	 */
	public CharacterList characters;
}
