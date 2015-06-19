package com.shadowgame.rpg.msg.login;

import java.util.List;

import com.shadowgame.rpg.net.msg.Message;

/**
 * 登录协议响应
 * @author wcj10051891@gmail.com
 * @date 2015年6月18日 下午5:02:48
 */
public class S_LoginResult extends Message {
	/**
	 * 字节
	 */
	public byte byteValue;
	/**
	 * 短整型
	 */
	public short shortValue;
	/**
	 * 整型
	 */
	public int intValue;
	/**
	 * 长整型
	 */
	public long longValue;
	/**
	 * float浮点型
	 */
	public float floatValue;
	/**
	 * double浮点型
	 */
	public double doubleValue;
	/**
	 * 字符串
	 */
	public String stringValue;
	/**
	 * 数组[String]
	 */
	public List<String> nestValue1;
	/**
	 * 数组[S_LoginResultAttachment]
	 */
	public List<S_LoginResultAttachment> nestValue2;
}
