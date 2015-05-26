package com.shadowgame.rpg.modules.protocol.dto.player;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;

/**
 * @author wcj10051891@gmail.com
 */
@Msg(10004)
public class PlayerDto {
	public long id;
	public String nickname;
	public int level;
	public int exp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
}
