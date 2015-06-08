package com.shadowgame.rpg.ai.event;


public enum AIEvent {
	/**
	 * 被攻击
	 */
	ATTACKED,
	/**
	 * 目标远离或者最近一次攻击已经超时很久
	 */
	TIRED_ATTACKING_TARGET,
	/**
	 * 切换到更大仇恨的玩家
	 */
	MOST_HATED_CHANGED,
	/**
	 * 没事做
	 */
	NOTHING_TODO,
	/**
	 * 返回出生点
	 */
	BACK_HOME,
	/**
	 * 恢复满血
	 */
	RESTORED_HEALTH,
	/**
	 * 看见玩家
	 */
	SEE_PLAYER,
	/**
	 * 玩家离开视野
	 */
	NOT_SEE_PLAYER,
	/**
	 * 死亡
	 */
	DIED;
}
