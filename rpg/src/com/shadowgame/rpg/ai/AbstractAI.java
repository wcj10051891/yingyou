package com.shadowgame.rpg.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.ai.action.AIAction;
import com.shadowgame.rpg.ai.event.AIEvent;
import com.shadowgame.rpg.ai.event.handler.EventHandler;
import com.shadowgame.rpg.ai.state.AIState;
import com.shadowgame.rpg.ai.state.impl.StateHandler;
import com.shadowgame.rpg.modules.fight.AbstractFighter;

public abstract class AbstractAI implements Runnable {

	protected AbstractFighter fighter;
	protected Map<AIEvent, EventHandler> eventHandlers = new HashMap<>();
	protected Map<AIState, StateHandler> stateHandlers = new HashMap<>();
	protected PriorityQueue<AIAction> actions = new PriorityQueue<>();
	protected AIState aiState = AIState.STOP;
	protected boolean isStateChanged;
	private Future<?> aiTask;
	
	public AbstractAI(AbstractFighter fighter) {
		this.fighter = fighter;
	}

	public void handleEvent(AIEvent event) {
		EventHandler handler = this.eventHandlers.get(event);
		if (handler != null)
			handler.handleEvent(this);
	}

	public AbstractFighter getOwner() {
		return fighter;
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

	public synchronized void setAiState(AIState aiState) {
		if (this.aiState != aiState) {
			this.aiState = aiState;
			isStateChanged = true;
		}
		start();
	}

	private void analyzeState() {
		isStateChanged = false;
		StateHandler stateHandler = stateHandlers.get(aiState);
		if (stateHandler != null)
			stateHandler.handleState(this);
	}

	/**
	 * 定时执行<br/>
	 * 遍历行为列表(每个Action表示一段逻辑，表示智能体的思考后的行为，可以将要执行的逻辑封装为action，想执行的时候加进action列表)，<br/>
	 * 执行action逻辑，执行的结果来决定这个action是否执行完了可以移除，如果没执行完则不移除，下次调度再次执行<br/>
	 * 当action列表空了，或者任何时候AI改变了当前状态，会重新切换到当前最新状态来思考<br/>
	 */
	@Override
	public void run() {
		for (Iterator<AIAction> it = this.actions.iterator(); it.hasNext();) {
			AIAction action = it.next();
			if (!action.execute(this)) {
				action.onRemove(this);
				it.remove();
			}
		}
		if (actions.isEmpty() || isStateChanged) {
			analyzeState();
		}
	}

	public void addAction(AIAction action) {
		synchronized (actions) {
			for (Iterator<AIAction> it = this.actions.iterator(); it.hasNext();) {
				AIAction old = it.next();
				if (action.equals(old)) {
					it.remove();
					if (action != old)
						action.increasePower(old.getPower());
					break;
				}
			}
			this.actions.add(action);
		}
	}

	public boolean removeAction(AIAction action) {
		synchronized (actions) {
			return actions.remove(action);
		}
	}

	public void clearActions() {
		synchronized (actions) {
			AIAction action = null;
			while ((action = actions.poll()) != null)
				action.onRemove(this);
		}
	}
	
	private void start() {
		if (!isStart()) {
//			aiTask = Services.timerService.jdkScheduler.scheduleAtFixedRate(
//					this, 1, 1, TimeUnit.SECONDS);
			aiTask = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
		}
	}

	private boolean isStart() {
		return aiTask != null && !aiTask.isCancelled();
	}

	public void stop() {
		if (aiTask != null && !aiTask.isCancelled()) {
			this.clearActions();
			aiTask.cancel(true);
			aiTask = null;
		}
	}

}
