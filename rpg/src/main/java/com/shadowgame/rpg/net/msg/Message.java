package com.shadowgame.rpg.net.msg;

import xgame.core.util.StringUtils;

public abstract class Message {

	@Override
	public String toString() {
		return StringUtils.toString(this, false);
	}
}
