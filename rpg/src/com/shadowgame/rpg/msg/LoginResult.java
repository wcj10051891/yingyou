package com.shadowgame.rpg.msg;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.net.msg.Message;

@Msg(10004)
public class LoginResult extends Message {
	public int i;
	public long l;
	public float f;
	public double d;
	public String s;
	public List<LoginResult2> r2;
}
