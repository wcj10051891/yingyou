package com.shadowgame.rpg.modules.protocol.dto;

public class ResponseDto {
	/**
	 * 序号
	 */
	public int sn;
	/**
	 * 是否是错误响应
	 */
	public boolean isError;
	/**
	 * 响应结果dto
	 */
	public ProtobufDto result;
}