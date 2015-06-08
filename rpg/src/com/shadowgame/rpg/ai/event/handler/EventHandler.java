package com.shadowgame.rpg.ai.event.handler;

import com.shadowgame.rpg.ai.AbstractAI;

/**
 * AI event事件处理器
 * @author wcj10051891@gmail.com
 * @Date 2015年6月8日 下午6:03:57
 */
public interface EventHandler {
	/**
	 * 根据AI体当前的各种情况来切换到不同的State
	 * @param ai
	 */
	void handleEvent(AbstractAI ai);
}
