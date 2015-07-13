package com.shadowgame.rpg.msg.login_11;

import xgame.core.util.CommonUtils;

import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.Message;

/**
 * 选择角色成功进入游戏
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午12:56:26
 */
public class Sc_11002 extends Message {
	/**
	 * 主角playerId
	 */
	public long playerId;
	/**
	 * 登录后进入的场景
	 */
	public int mapId;
	/**
	 * 上次的场景坐标x
	 */
	public short x;
	/**
	 * 上次的场景坐标z
	 */
	public short z;
	/**
	 * 上次的场景坐标y
	 */
	public short y;
	
	public Sc_11002 from(Player player) {
		this.playerId = player.getObjectId();
		Position pos = player.getPosition();
		this.mapId = pos.getMapInstance().getId();
		this.x = (short)pos.getX();
		this.z = (short)pos.getY();
		this.y = CommonUtils.nullIntegerToDef(player.entity.lastMapZ).shortValue();
		return this;
	}
}
