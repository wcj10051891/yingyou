package com.shadowgame.rpg.msg.map_12;

import com.shadowgame.rpg.net.msg.Message;

/**
 * npc信息
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午3:51:06
 */
public class NpcInfo extends Message {
	/**
	 * npc配置id
	 */
	public int modelId;
	/**
	 * 唯一id
	 */
	public long id;
	/**
	 * 名称
	 */
	public String name;
}
