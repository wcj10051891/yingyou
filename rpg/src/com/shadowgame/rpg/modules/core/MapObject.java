package com.shadowgame.rpg.modules.core;

import java.util.Collection;

import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.map.World;
import com.shadowgame.rpg.util.RuntimeUniqueId;

/**
 * 地图上的对象
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月28日 上午11:44:55
 */
public abstract class MapObject {
	/**
	 * 对象唯一id
	 */
	protected Integer objectId;
	/**
	 * 位置
	 */
	protected Position position;
	
	public MapObject() {
		this.objectId = RuntimeUniqueId.next(MapObject.class);
	}

	public Integer getObjectId() {
		return objectId;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Collection<MapObject> getSeeObjects() {
		return this.position.getMapRegion().getMapObjects();
	}
	
	public <T extends MapObject> Collection<T> getSeeObjectsByType(Class<T> objectType) {
		return this.position.getMapRegion().getMapObjectByType(objectType);
	}
	
	public World getWorld() {
		return this.position.getMapRegion().getMapInstance().getGameMap().getWorld();
	}
}
