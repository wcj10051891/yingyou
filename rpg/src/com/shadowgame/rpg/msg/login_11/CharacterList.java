package com.shadowgame.rpg.msg.login_11;

import java.util.ArrayList;
import java.util.List;

import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.persist.entity.TPlayer;

/**
 * 角色列表
 * @author wcj10051891@gmail.com
 * @date 2015年6月27日 下午12:46:08
 */
public class CharacterList extends Message {
	/**
	 * 角色列表
	 */
	public List<CharacterInfo> characters;
	
	public CharacterList from(List<TPlayer> entitys) {
		if(!entitys.isEmpty()) {
			List<CharacterInfo> cs = new ArrayList<CharacterInfo>();
			for (TPlayer entity : entitys)
				cs.add(new CharacterInfo().from(entity));
			this.characters = cs;
		}
		return this;
	}

}
