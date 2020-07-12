package com.shadowgame.rpg.msg.login_11;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.net.msg.NoPlayerClientMsg;
import com.shadowgame.rpg.service.Services;

/**
 * 选择角色
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午12:56:26
 */
public class Cs_11002 extends NoPlayerClientMsg {

	/**
	 * 选择角色的playerId
	 */
	public long playerId;
	
	@Override
	public void handleNoPlayer(ChannelHandlerContext ctx) {
		if(playerId <= 0)
			throw new NoticeException("玩家id必须大于0");
		Player player = Services.cacheService.get(playerId, Player.class, true);
		if(player == null)
			throw new NoticeException("玩家不存在");
		player.onLogin(ctx.getChannel());
	}

}
