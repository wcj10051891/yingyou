package com.shadowgame.rpg.msg.login_11;

import java.util.List;

import com.shadowgame.rpg.net.msg.Message;


/**
 * 创建角色响应
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午12:20:09
 */
public class Sc_11001 extends Message {
	/**
	 * 角色列表
	 */
	public List<CharacterInfo> characters;
}