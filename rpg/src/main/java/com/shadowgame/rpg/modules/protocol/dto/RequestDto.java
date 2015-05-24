package com.shadowgame.rpg.modules.protocol.dto;

/**
 * 请求
 * @author wcj10051891@gmail.com
 */
public class RequestDto {
	/**
	 * 请求序号
	 */
	public int sn;
	/**
	 * 服务类
	 */
	public String service;
	/**
	 * 服务类中的方法
	 */
	public String method;
	/**
	 * 参数列表:json数组，按照service方法的参数个数及类型一一匹配
	 */
	public String params;
}