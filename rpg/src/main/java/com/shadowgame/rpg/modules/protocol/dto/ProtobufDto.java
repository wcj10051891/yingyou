package com.shadowgame.rpg.modules.protocol.dto;

import com.google.protobuf.ByteString;


/**
 * protobuf对象，用类型和二进制描述一个对象
 * @author wcj10051891@gmail.com
 */
public class ProtobufDto {
	/**
	 * protobuf message类型的数字id
	 */
	public int typeId;
	/**
	 * protobuf message的二进制数据
	 */
	public ByteString data;
}