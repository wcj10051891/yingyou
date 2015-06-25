package com.shadowgame.rpg.msg.core_10;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 提示消息
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午8:26:14
 */
public class Sc_10000 extends Message {
	/**
	 * 0飘字提示，1弹窗提示
	 */
	public int type;
	/**
	 * 提示内容
	 */
	public String content;
	
	public Sc_10000() {
	}
	
	public Sc_10000(String content) {
		this.content = content;
	}

	public Sc_10000(int type, String content) {
		super();
		this.type = type;
		this.content = content;
	}
}
