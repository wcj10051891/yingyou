package com.shadowgame.rpg.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import xgame.core.util.RandomUtils;

import com.shadowgame.rpg.core.AppConfig;

/**
 * 全局唯一id，跨服务器唯一
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午3:54:28
 */
public abstract class UniqueId {
	private static final String CAPITAL_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Map<String, Integer> letter2index = new HashMap<String, Integer>(26);
	private static final Map<Integer, String> index2letter = new HashMap<Integer, String>(26);
	static {
		for(int i = 0; i < 26; i++) {
			String s = String.valueOf(CAPITAL_LETTER.charAt(i));
			letter2index.put(s, i);
			index2letter.put(i, s);
		}
	}
	private static final int serverId = letter2index.get(AppConfig.SERVER_KEY) << 12 | AppConfig.SERVER_SEQ;
	/**
	 * 产生一个64位的唯一id，不同服务器间不会重复。
	 * @return
	 */
	public static synchronized Long next() {
		try {
			Thread.sleep(1l);
		} catch (InterruptedException e) {
		}
		return encode();
	}

	/**
	 * 64位唯一id=42位utc秒数+5位服务器单字母标记+12位服务器唯一序号+5位随机数
	 */
	private static long encode() {
		long n = RandomUtils.nextRandomInt(0, 31) & 0x1F;
		long utc = System.currentTimeMillis();
		long result = utc << 22;
		result |= serverId << 5;
		result |= n;
		return result;
	}

	/**
	 * @see UniqueId#encode(long)
	 * @param encoded
	 * @return [utc毫秒, server编号, server标识, 随机数]
	 */
	private static Object[] decode(long encoded) {
		long serverKey = encoded >> 17 & 0x1F;
		long serverSeq = encoded >> 5 & 0xFFF;
		long utc = encoded >> 22 & 0x3FFFFFFFFFFl;
		long n = encoded & 0x1F;
		return new Object[]{
			utc,
			index2letter.get((int)(serverKey)), 
			serverSeq, 
			n,
		};
	}
	
	private static String print(long encoded) {
		Object[] decode = decode(encoded);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis((Long)(decode[0]));
		return decode[1] + "" + decode[2] + ":" + c.getTime() + "," + decode[3];
	}
	
	public static void main(String[] args) {
		long l = next();
		System.out.println(l);
		System.out.println(print(l));
	}
}
