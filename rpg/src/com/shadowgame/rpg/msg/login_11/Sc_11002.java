package com.shadowgame.rpg.msg.login_11;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 选择角色成功进入游戏
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午12:56:26
 */
public class Sc_11002 extends Message {

	/**
	 * 登录后进入的场景
	 */
	public int mapId;
	
	public Sc_11002 from(int mapId) {
		this.mapId = mapId;
		return this;
	}
}
