package com.shadowgame.rpg.msg.map_12;

import java.util.List;

import com.shadowgame.rpg.modules.map.GameMap;
import com.shadowgame.rpg.modules.map.MapInstance;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 地图
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午2:28:36
 */
public class Sc_12001 extends Message {
	/**
	 * 地图配置id
	 */
	public int modelId;
	/**
	 * 地图唯一id
	 */
	public int id;
	/**
	 * 地图名称
	 */
	public String name;
	/**
	 * 宽
	 */
	public int width;
	/**
	 * 高
	 */
	public int height;
	/**
	 * 传送点列表
	 */
	public List<TeleporterInfo> teleporters;
	/**
	 * npc列表
	 */
	public List<NpcInfo> npcs;
	
	public Sc_12001 from(MapInstance map) {
		GameMap gameMap = map.getGameMap();
		this.height = gameMap.entity.height;
		this.width = gameMap.entity.width;
		this.id = map.getId();
		this.modelId = gameMap.entity.id;
		this.name = gameMap.entity.name;
		return this;
	}
}
