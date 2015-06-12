package com.shadowgame.rpg.msg;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;

@Msg(10003)
public class ProtoMsg {
	/**
	 * 协议号
	 */
	public int id;
	/**
	 * proto数据
	 */
	public byte[] data;
}
