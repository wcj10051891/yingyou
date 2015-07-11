package com.shadowgame.rpg.modules.cooldown;

import java.util.HashMap;
import java.util.Map;


/**
 * 冷却
 * @author wcj10051891@gmail.com
 * @date 2015年7月10日 下午6:03:48
 */
public class Cooldown {
	private Map<String, CooldownItem> items = new HashMap<String, CooldownItem>();
	
	public void add(CooldownType type, String key, int cooldownSeconds) {
		this.items.put(key(type, key), new CooldownItem(System.currentTimeMillis(), cooldownSeconds));
	}
	
	public boolean isCooldown(CooldownType type, String key, int cooldownSeconds) {
		CooldownItem item = this.items.get(key(type, key));
		if(item == null)
			return false;
		return item.isCooldown();
	}
	
	private String key(CooldownType type, String key) {
		return type.name() + "_" + key;
	}
}
