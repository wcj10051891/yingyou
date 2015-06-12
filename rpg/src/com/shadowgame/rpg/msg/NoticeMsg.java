package com.shadowgame.rpg.msg;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 飘字提示
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月27日 下午2:03:06
 */
@Msg(10002)
public class NoticeMsg extends Message {
	/**
	 * 飘字提示消息内容
	 */
	public String content;

	public NoticeMsg() {
	}

	public NoticeMsg(String content) {
		super();
		this.content = content;
	}
}
