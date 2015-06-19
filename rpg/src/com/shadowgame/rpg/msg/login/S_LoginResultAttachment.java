package com.shadowgame.rpg.msg.login;

import com.shadowgame.rpg.net.msg.Message;

/**
 * loginResult协议的嵌套消息
 * @author wcj10051891@gmail.com
 * @date 2015年6月19日 上午11:43:22
 */
public class S_LoginResultAttachment extends Message {
	/**
	 * 字符串
	 */
	public String s;
	
	public S_LoginResultAttachment() {
	}

	public S_LoginResultAttachment(String s) {
		super();
		this.s = s;
	}
}
