package com.shadowgame.rpg.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.ai.event.AIEvent;
import com.shadowgame.rpg.ai.event.handler.EventHandler;
import com.shadowgame.rpg.ai.intent.Intent;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.ai.state.impl.StateHandler;
import com.shadowgame.rpg.modules.core.AbstractSpirit;

public abstract class AbstractAI implements Runnable {

	protected AbstractSpirit spirit;
	protected Map<AIEvent, EventHandler> eventHandlers = new HashMap<>();
	protected Map<AIState, StateHandler> stateHandlers = new HashMap<>();
	protected PriorityQueue<Intent> intents = new PriorityQueue<>();
	protected AIState aiState = AIState.NONE;
	protected boolean isStateChanged;
	private Future<?> aiTask;
	
	public AbstractAI(AbstractSpirit spirit) {
		this.spirit = spirit;
	}

	public void handleEvent(AIEvent event) {
		EventHandler handler = this.eventHandlers.get(event);
		if (handler != null)
			handler.handleEvent(this);
	}

	public AbstractSpirit getOwner() {
		return spirit;
	}

	public void setOwner(AbstractSpirit spirit) {
		this.spirit = spirit;
	}

	protected void addEventHandler(AIEvent aiEvent, EventHandler eventHandler) {
		this.eventHandlers.put(aiEvent, eventHandler);
	}

	protected void addStateHandler(AIState aiState, StateHandler stateHandler) {
		this.stateHandlers.put(aiState, stateHandler);
	}

	public AIState getAiState() {
		return this.aiState;
	}

	public void setAiState(AIState aiState) {
		if (this.aiState != aiState) {
			this.aiState = aiState;
			isStateChanged = true;
		}
	}

	public void analyzeState() {
		isStateChanged = false;
		StateHandler stateHandler = stateHandlers.get(aiState);
		if (stateHandler != null)
			stateHandler.handleState(this);
	}

	/**
	 * 定时执行<br/>
	 * 遍历intent指令序列(每个intent表示一段逻辑，可以是任何意图，想做的事情，表示智能体的思考后的行为，可以将要执行的逻辑封装为intent，想执行的时候加进intent列表)，<br/>
	 * 执行intent逻辑，执行的结果来决定这个intent是否执行完了可以移除，如果没执行完则不移除，下次调度再次执行<br/>
	 * 当intent序列空了，或者任何时候AI当前状态改变了，会重新切换到当前最新状态来思考<br/>
	 */
	@Override
	public void run() {
		for (Iterator<Intent> it = this.intents.iterator(); it.hasNext();) {
			Intent intent = it.next();
			if (!intent.execute(this)) {
				intent.onRemove(this);
				it.remove();
			}
		}
		if (intents.isEmpty() || isStateChanged) {
			analyzeState();
		}
	}

	public void addIntent(Intent intent) {
		synchronized (intents) {
			for (Iterator<Intent> it = this.intents.iterator(); it.hasNext();) {
				Intent old = it.next();
				if (intent.equals(old)) {
					it.remove();
					if (intent != old)
						intent.increasePower(old.getPower());
					break;
				}
			}
			this.intents.add(intent);
		}
	}

	public boolean removeIntent(Intent intent) {
		synchronized (intents) {
			return intents.remove(intent);
		}
	}

	public void clearIntents() {
		synchronized (intents) {
			Intent intent = null;
			while ((intent = intents.poll()) != null)
				intent.onRemove(this);
		}
	}
	
	public void start() {
		if (!isStart()) {
//			aiTask = Services.timerService.jdkScheduler.scheduleAtFixedRate(
//					this, 1, 1, TimeUnit.SECONDS);
			aiTask = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
		}
	}

	public boolean isStart() {
		return aiTask != null && !aiTask.isCancelled();
	}

	public void stop() {
		if (aiTask != null && !aiTask.isCancelled()) {
			aiTask.cancel(true);
			aiTask = null;
		}
	}

}
