package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 传送点
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午3:48:02
 */
public class TeleporterInfo extends Message {
	/**
	 * 传送点id
	 */
	public int id;
	/**
	 * 传送点名称
	 */
	public String name;
}
