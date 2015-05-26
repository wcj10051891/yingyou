package com.shadowgame.rpg.modules.protocol.dto;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;

@Msg(10002)
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