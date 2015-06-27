package com.shadowgame.rpg.msg.login_11;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.shadowgame.rpg.core.AlertException;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.msg.core_10.Sc_10000;
import com.shadowgame.rpg.net.msg.NoPlayerClientMsg;
import com.shadowgame.rpg.persist.dao.TPlayerDao;
import com.shadowgame.rpg.service.Services;

/**
 * 登录请求
 * @author wcj10051891@gmail.com
 * @date 2015年6月25日 上午10:50:07
 */
public class Cs_11000 extends NoPlayerClientMsg {
	
	/**
	 * 用户名
	 */
	public String username;
	
	/**
	 * 处理player登录
	 */
	@Override
	public void handleNoPlayer(ChannelHandlerContext ctx) {
		if(ctx.getChannel().getAttachment() != null)
			throw new AlertException("已登录");
		
		Player player = Services.app.world.allPlayers.getOnlinePlayerByUser(username);
		if(player != null) {
			player.send(new Sc_10000("账号重复登录，您已下线"));
			player.disconnect();
		}
		
		Sc_11000 result = new Sc_11000();
		result.characters = new CharacterList().from(Services.daoFactory.get(TPlayerDao.class).getByUsername(username));
		Services.tcpService.send(result, ctx.getChannel());
		
//		Player player = Services.cacheService.get(4672337295849525248l, Player.class, true);
//		player.onLogin(ctx.getChannel());
//		GameMap gameMap = Services.app.world.gameMaps.get(1);
//		MapInstance target = gameMap.getDefaultInstance();
//		Services.app.world.updatePosition(player, target, 0, 0);
//		
//		LinkedList<Point> newPath = PathFinding.find(gameMap, player.getPosition().getPoint(), new Point(98, 400));
//		player.getMoveManager().start(newPath);
	}
}