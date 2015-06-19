package com.shadowgame.rpg.msg.login;

import com.shadowgame.rpg.net.msg.Message;

/**
 * login协议内的嵌套消息
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午4:36:28
 */
public class C_LoginAttachment extends Message {
	/**
	 * 字符串
	 */
	public String s;
	
	public C_LoginAttachment() {
	}

	public C_LoginAttachment(String s) {
		super();
		this.s = s;
	}
	
}
