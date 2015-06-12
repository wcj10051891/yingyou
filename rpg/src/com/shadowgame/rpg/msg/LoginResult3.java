package com.shadowgame.rpg.msg;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.net.msg.Message;

@Msg(10006)
public class LoginResult3 extends Message {
	public String s;
}
