package com.shadowgame.rpg.ai.event;


public enum AIEvent {
	/**
	 * 被攻击
	 */
	ATTACKED,
	/**
	 * 停止AI计算
	 */
	STOP_AI,
	/**
	 * 看见玩家
	 */
	SEE_PLAYER,
	/**
	 * 有玩家离开视野
	 */
	NOT_SEE_PLAYER,
	/**
	 * 思考
	 */
	THINKING;
}
