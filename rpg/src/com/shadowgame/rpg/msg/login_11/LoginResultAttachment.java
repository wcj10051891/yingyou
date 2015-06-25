package com.shadowgame.rpg.msg.login_11;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 登录响应内的嵌套消息
 * @author wcj10051891@gmail.com
 * @date 2015年6月19日 上午11:43:22
 */
public class LoginResultAttachment extends Message {
	/**
	 * 字符串
	 */
	public String s;
	
	public LoginResultAttachment() {
	}

	public LoginResultAttachment(String s) {
		super();
		this.s = s;
	}
}
