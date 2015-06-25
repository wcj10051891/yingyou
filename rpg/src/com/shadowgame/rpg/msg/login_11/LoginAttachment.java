package com.shadowgame.rpg.msg.login_11;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 登录协议内的嵌套消息
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午4:36:28
 */
public class LoginAttachment extends Message {
	/**
	 * 字符串
	 */
	public String s;
	
	public LoginAttachment() {
	}

	public LoginAttachment(String s) {
		super();
		this.s = s;
	}
	
}
