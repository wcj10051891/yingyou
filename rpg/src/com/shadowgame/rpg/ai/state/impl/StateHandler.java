package com.shadowgame.rpg.ai.state.impl;

import com.shadowgame.rpg.ai.AbstractAI;

/**
 * 实现AI体处于不同的状态时候的处理逻辑，不同状态对应不同的一系列意图逻辑
 * @author wcj10051891@gmail.com
 * @Date 2015年6月8日 下午5:44:57
 */
public interface StateHandler {
	/**
	 * 根据AI体当前的各种情况进行处理，产生不同的意图intent逻辑，或切换到其他状态
	 * @param state
	 * @param ai
	 */
	public abstract void handleState(AbstractAI ai);
}
