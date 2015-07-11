package xgame.client;

import com.shadowgame.rpg.net.msg.ClientMsg;

public class ClientCommand {
	public ClientMsg msg;
	public long waitMills;

	public ClientCommand(ClientMsg msg, long waitMills) {
		this.waitMills = waitMills;
		this.msg = msg;
	}
}
