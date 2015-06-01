package com.shadowgame.rpg.msg;

import xgame.core.net.protocol.Msg;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 弹窗提示
 * @author wcj10051891@gmail.com
 * @Date 2015年5月27日 下午2:02:59
 */
@Msg(10001)
public class AlertMsg extends Message {
	/**
	 * 弹窗提示消息内容
	 */
	private String content;
	
	public AlertMsg() {
	}

	public AlertMsg(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
