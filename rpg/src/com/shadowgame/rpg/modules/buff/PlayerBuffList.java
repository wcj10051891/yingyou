package com.shadowgame.rpg.modules.buff;

import java.util.Iterator;
import java.util.List;

import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.modules.player.Player;

public class PlayerBuffList extends BuffList {

	public PlayerBuffList(Player player) {
		super(player);
		if(StringUtils.hasText(player.entity.buff)) {
			List<JSONObject> _buffs = JSON.parseArray(player.entity.buff, JSONObject.class);
			for (Iterator<JSONObject> it = _buffs.iterator(); it.hasNext();) {
				BuffTask buff = new BuffTask(player, it.next());
				if(!buff.isFinish()) {
					buff.start();
				} else {
					it.remove();
				}
			}
		}
	}
	
	@Override
	public String toString() {
		JSONArray json = new JSONArray();
		for (BuffTask buff : this.buffs)
			json.add(buff.toJson());
		return json.toString();
	}
}
