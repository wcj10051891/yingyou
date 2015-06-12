package com.shadowgame.rpg.msg;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.net.msg.Message;

@Msg(10005)
public class LoginResult2 extends Message {
	public int i;
	public long l;
	public float f;
	public double d;
	public String s;
	public List<LoginResult3> r3;
}
