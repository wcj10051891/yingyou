package com.shadowgame.rpg.modules.protocol.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.modules.Controller;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.protocol.dto.chat.ChatDto;
import com.shadowgame.rpg.modules.protocol.dto.chat.ChatListDto;
import com.shadowgame.rpg.modules.protocol.dto.player.LoginResultDto;
import com.shadowgame.rpg.modules.protocol.dto.player.PlayerDto;
import com.shadowgame.rpg.persist.dao.PlayerDao;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.UniqueId;

@Controller
public class PlayerCtrl {
	private static final PlayerDao playerDao = Services.daoFactory.get(PlayerDao.class);
	public static ThreadLocal<ChannelHandlerContext> loginChannel = new ThreadLocal<ChannelHandlerContext>();
	
	public LoginResultDto login(String username) {
		com.shadowgame.rpg.modules.core.Player onlinePlayer = Services.appService.world.allPlayers.getOnlinePlayerByUser(username);
		if(onlinePlayer != null) {
			onlinePlayer.disconnectWaitCompleted();
		}
		com.shadowgame.rpg.persist.entity.Player player = playerDao.getByUsername(username);
		LoginResultDto result = new LoginResultDto();
		if(player != null) {
			com.shadowgame.rpg.modules.core.Player p = new com.shadowgame.rpg.modules.core.Player();
			Services.cacheService.put(p.init(player));
			
			ChannelHandlerContext ctx = loginChannel.get();
			p.channelId = ctx.getChannel().getId();
			ctx.setAttachment(p);
			Services.tcpService.channels.put(p.channelId, ctx);
			
			p.onLogin();
			result.success = true;
			//字段列表，字段类型
			PlayerDto playerDto = new PlayerDto();
			playerDto.exp = player.exp;
			playerDto.id = player.id;
			playerDto.level = player.level;
			playerDto.nickname = player.nickname;
			result.playerDto = playerDto;
		}
		return result;
	}
	
	public PlayerDto create(String username, String nickname) {
		com.shadowgame.rpg.persist.entity.Player player = playerDao.getByUsername(username);
		if(player != null)
			throw new NoticeException("角色已经存在");
		
		com.shadowgame.rpg.persist.entity.Player entity = new com.shadowgame.rpg.persist.entity.Player();
		entity.createTime = new Timestamp(System.currentTimeMillis());
		entity.exp = 0;
		entity.extAttribute = "";
		entity.id = UniqueId.next();
		entity.level = 1;
		entity.nickname = nickname;
		entity.username = username;
		entity.loginTime = null;
		entity.logoutTime = null;
		playerDao.insert(entity);
		PlayerDto playerDto = new PlayerDto();
		playerDto.exp = entity.exp;
		playerDto.id = entity.id;
		playerDto.level = entity.level;
		playerDto.nickname = entity.nickname;
		return playerDto;
	}
	
	public void say(Player player, String content) {
		ChatDto dto = new ChatDto();
		dto.sayerId = player.getObjectId();
		dto.sayerNickname = player.entity.nickname;
		dto.content = content;
		Services.tcpService.world(dto);
	}
	
	public ChatListDto next() {
//		StopWatch watch = new StopWatch();
//		watch.start("get next id");
//		UniqueId.next();
		ChatListDto result = new ChatListDto();
		List<ChatDto> list = new ArrayList<ChatDto>();
		int n = 50;
		for(int i = 0; i < n; i++) {
			ChatDto c1 = new ChatDto();
			c1.content = "你好" + i;
			c1.sayerId = (long)i;
			c1.sayerNickname = "昵称" + i;
			list.add(c1);
		}
		result.chats = list;
		List<Integer> l2 = new ArrayList<Integer>();
		for(int i = 0; i < n; i++) {
			l2.add(i);
		}
		result.chats2 = l2;
		return result;
//		watch.stop();
//		System.out.println(watch.prettyPrint());
//		CountMap<Integer, Integer> give = new CountMap<>();
//		give.put(1, 100);
//		try {
//			player.give(give);
//		} catch (Exception e) {
//			throw new AppException("赠送物品失败：" + e.getMessage());
//		}
	}
	

	public void logout(Player player) {
		player.disconnect(false);
	}
}
