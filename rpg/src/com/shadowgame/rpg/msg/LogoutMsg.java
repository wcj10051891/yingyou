package com.shadowgame.rpg.msg;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.ClientMsg;

@Msg(10007)
public class LogoutMsg extends ClientMsg {

	@Override
	public void handle(Player player) {
		player.disconnect(true);
	}

}
