package com.shadowgame.rpg.msg.login_11;

import java.util.ArrayList;

import org.jboss.netty.channel.ChannelHandlerContext;

import xgame.core.util.StringUtils;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.NoPlayerClientMsg;
import com.shadowgame.rpg.persist.dao.TPlayerDao;
import com.shadowgame.rpg.persist.entity.TPlayer;
import com.shadowgame.rpg.service.Services;

/**
 * 创建角色
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午12:20:09
 */
public class Cs_11001 extends NoPlayerClientMsg {
	/**
	 * 用户名
	 */
	public String username;
	/**
	 * 昵称
	 */
	public String nickname;
	/**
	 * 职业，1战士2法师3弓箭手
	 */
	public byte vocation;

	@Override
	public void handleNoPlayer(ChannelHandlerContext ctx) {
		TPlayerDao playerDao = Services.daoFactory.get(TPlayerDao.class);
		if(!StringUtils.hasText(username))
			throw new NoticeException("用户名不能为空");
		if(!StringUtils.hasText(nickname))
			throw new NoticeException("昵称不能为空");
		if(!(vocation >= 1 && vocation <= 3))
			throw new NoticeException("职业不存在");
		TPlayer entity = playerDao.getByNickname(nickname);
		if(entity != null)
			throw new NoticeException("昵称已经存在");
		entity = Player.createEntity(username, nickname, vocation);
		try {
			playerDao.insert(entity);
		} catch (Exception e) {
			throw new AppException("create character error", e);
		}
		Sc_11001 result = new Sc_11001();
		result.characters = new ArrayList<CharacterInfo>();
		result.characters.add(new CharacterInfo().from(entity));
		Services.tcpService.send(result, ctx.getChannel());
	}
}
