package com.shadowgame.rpg.modules.core;

import com.shadowgame.rpg.modules.map.Position;

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
	protected Long objectId;
	/**
	 * 位置
	 */
	protected Position position;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
