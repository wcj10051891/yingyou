package com.shadowgame.rpg.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
	/**
	 * 产生一个64位的唯一id，不同服务器间不会重复。
	 * 64位唯一id=43位utc时间戳 +5位服务器单字母标记+16位服务器唯一序号
	 * @return
	 */
	public static Long next() {
		return encode(nextMillis());
	}

	private static synchronized long nextMillis() {
		try {
			Thread.sleep(1l);
		} catch (Exception e) {
		}
		return System.currentTimeMillis();
	}

	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
	private static final DateTime date19700101 = new DateTime(1970, 1, 1, 0, 0, 0);
	
	private static long encodeUTC(long utcMillis) {
		DateTime dateTime = new DateTime(utcMillis);
		long days = Days.daysBetween(date19700101, dateTime).getDays();
		long hour = dateTime.getHourOfDay();
		long minute = dateTime.getMinuteOfHour();
		long second = dateTime.getSecondOfMinute();
		long mills = dateTime.getMillisOfSecond();
		long n = days << 27;//(43 - 16);
		n |= (hour << 22);//(43 - 5 - 16));
		n |= (minute << 16);//(43 - 6 - 16 - 5));
		n |= (second << 10);//(43 - 6 - 16 - 5 - 6));
		n |= mills;
		return n;
	}
	
	private static long decodeUTC(long encoded) {
		int days = (int)(encoded >> 27 & 0xffff);
		int hour = (int)(encoded >> 22 & 0x1f);
		int minute = (int)(encoded >> 16 & 0x3f);
		int second = (int)(encoded >> 10 & 0x3f);
		int mills = (int)(encoded & 0x3ff);
		String datestr = new StringBuilder()
		.append(date19700101.plusDays(days).toString("yyyyMMdd"))
		.append(hour < 10 ? "0" + hour : hour)
		.append(minute < 10 ? "0" + minute : minute)
		.append(second < 10 ? "0" + second : second)
		.append(mills < 10 ? "00" + mills : mills < 100 ? "0" + mills : mills)
		.toString();
		DateTime dateTime = formatter.parseDateTime(datestr);
		return dateTime.getMillis();
	}
	/**
	 * 64位唯一id=43位utc时间戳+5位服务器单字母标记+16位服务器唯一序号
	 * 16位天数：20991231-19700101日期相隔天数 最大值47481<br/>
	 * 5位小时：最大值23<br/>
	 * 6位分钟：最大值59<br/>
	 * 6位秒钟：最大值59<br/>
	 * 10位毫秒：最大值999<br/>
	 * 5位服务器单字母标记（26个字母）
	 * 16位服务器序号：最大值32767
	 */
	private static long encode(long original) {
		long utc = encodeUTC(original);
		long serverKey = letter2index.get(AppConfig.SERVER_KEY);
		long serverSeq = AppConfig.SERVER_SEQ;
		long n = utc << 21;
		n |= (serverKey << 16);
		n |= serverSeq;
		return n;
	}
	
	/**
	 * @see UniqueId#encode(long)
	 * @param encoded
	 * @return [utc时间, server标识, server编号]
	 */
	private static Object[] decode(long encoded) {
		return new Object[]{
			decodeUTC(encoded >> 21), 
			index2letter.get((int)(encoded >> 16 & 0x1f)), 
			encoded & 0xffff};
	}
	
	public static void main(String[] args) {
//		System.out.println(Arrays.toString(decode(4501493532967145472l)));
		
//		StopWatch s = new StopWatch();
//		s.start();
//		System.out.println(next());
//		System.out.println(next());
//		s.stop();
//		System.out.println(s.prettyPrint());
		Long next = next();
		System.out.println(next);
		Object[] decode = decode(next);
		System.out.println(Arrays.toString(decode));
//		System.out.println(new Timestamp((Long)decode[0]));
//		for(int i = 0; i < 100; i++)
//			System.out.println(next());
//		StopWatch s = new StopWatch();
//		s.start();
//		System.out.println(next());
//		s.stop();
//		System.out.println(s.prettyPrint());
	}
}
