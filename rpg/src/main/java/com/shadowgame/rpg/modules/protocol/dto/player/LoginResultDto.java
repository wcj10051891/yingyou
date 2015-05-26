package com.shadowgame.rpg.modules.protocol.dto.player;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;

/**
 * @author wcj10051891@gmail.com
 */
@Msg(10003)
public class LoginResultDto {
	public boolean success;
	public PlayerDto playerDto;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public PlayerDto getPlayerDto() {
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}
}
