package com.shadowgame.rpg.ai.event;


public enum AIEvent {
	/**
	 * 被攻击
	 */
	ATTACKED,
	/**
	 * 切换到更大仇恨的玩家
	 */
	MOST_HATED_CHANGED,
	/**
	 * 停止AI计算
	 */
	STOP,
	/**
	 * 返回出生点
	 */
	BACK_HOME,
	/**
	 * 看见玩家
	 */
	SEE_PLAYER,
	/**
	 * 有玩家离开视野
	 */
	NOT_SEE_PLAYER,
	/**
	 * 死亡
	 */
	DIED,
	/**
	 * 思考
	 */
	THINKING;
}
