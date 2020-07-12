package com.shadowgame.rpg.net.msg;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.player.Player;

public abstract class NoPlayerClientMsg extends ClientMsg {
	
	@Override
	public void handle(Player player) {
		throw new AppException("no player, cannot handle");
	}
	
	public abstract void handleNoPlayer(ChannelHandlerContext ctx);
}
