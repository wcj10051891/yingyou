package com.shadowgame.rpg.modules.common;

import org.joda.time.DateTime;

import xgame.core.util.JsonUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * daily属性，一天内有效
 * json格式，key为日期yyyyMMdd，value为任意对象
 * 读取设置值的时候，会比较日期，若日期不一致会废弃之前的value
 * @author wcj10051891@gmail.com
 */
public class DailyAttribute {
	private JSONObject json;

	public DailyAttribute() {
		this.json = new JSONObject();
		checkExpire();
	}
	
	public DailyAttribute(String json) {
		this.json = JsonUtils.parseObject(json);
		checkExpire();
	}
	
	public DailyAttribute(JSONObject json) {
		this.json = json;
		checkExpire();
	}
	
	public JSONObject checkExpire() {
		String date = this.json.getString("date");
		String today = new DateTime().toString("yyyyMMdd");
		if(date == null || !date.equals(today)) {
			this.json.clear();
			this.json.put("date", today);
		}
		return this.json;
	}
	
	public <T> T getObject(String key, Class<T> clazz) {
		return checkExpire().getObject(key, clazz);
	}
	
	public Object get(String key) {
		return checkExpire().get(key);
	}

	public Integer getInt(String key) {
		return checkExpire().getInteger(key);
	}

	public Long getLong(String key) {
		return checkExpire().getLong(key);
	}
	
	public String getString(String key) {
		return checkExpire().getString(key);
	}
	
	public boolean getBoolean(String key) {
		return checkExpire().getBooleanValue(key);
	}
	
	public JSONArray getJSONArray(String key) {
		return checkExpire().getJSONArray(key);
	}
	
	public boolean containsKey(String key) {
		return checkExpire().containsKey(key);
	}
	
	public void put(String key, Object value) {
		checkExpire().put(key, value);
	}
	
	public int add(String key) {
		return add(key, 1);
	}
	
	public int add(String key, int add) {
		checkExpire();
		if(!this.json.containsKey(key))
			this.json.put(key, 0);
		int v = this.json.getIntValue(key);
		this.json.put(key, v + add);
		return this.json.getIntValue(key);
	}
	
	public int reduce(String key) {
		return reduce(key, 1);
	}
	
	public int reduce(String key, int reduce) {
		return reduce(key, reduce, true);
	}
	
	public int reduce(String key, int reduce, boolean minzero) {
		checkExpire();
		if(!this.json.containsKey(key))
			this.json.put(key, 0);
		int v = this.json.getIntValue(key);
		if(minzero && v <= 0) {
			this.json.put(key, 0);
			return v;
		}
		v -= reduce;
		if(minzero && v < 0)
			v = 0;
		this.json.put(key, v);
		return v;
	}
	
	public void remove(String key) {
		checkExpire().remove(key);
	}
	
	/**
	 * @return
	 */
	@Override
	public String toString() {
		return this.json.toString();
	}
	
	public static void main(String[] args) {
		DailyAttribute daily = new DailyAttribute();
		daily.add("count");
		daily.add("count");
		daily.add("count", 100);
		daily.reduce("count", 100);
		daily.reduce("counts", 100, false);
		JSONObject j = JsonUtils.parseObject("{'a':'b','c':'d'}");
		daily.put("missions", j);
		System.out.println(daily.get("missions").getClass());
		System.out.println(daily);
	}
}
