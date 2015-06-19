package com.shadowgame.rpg.msg.core;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 提示消息
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午8:26:14
 */
public class S_Tips extends Message {
	/**
	 * 0飘字提示，1弹窗提示
	 */
	public int type;
	/**
	 * 提示内容
	 */
	public String content;
	
	public S_Tips() {
	}
	
	public S_Tips(String content) {
		this.content = content;
	}

	public S_Tips(int type, String content) {
		super();
		this.type = type;
		this.content = content;
	}
}
