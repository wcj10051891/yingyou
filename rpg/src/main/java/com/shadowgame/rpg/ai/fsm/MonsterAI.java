package com.shadowgame.rpg.ai.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonsterAI extends BaseFSM {
	private static final Logger log = LoggerFactory.getLogger(MonsterAI.class);
	
	public static class IdleState extends BaseState {
		@Override
		public void onEnter() {
			log.info("enter " + this.getClass() + " state.");
		}

		@Override
		public void execute() {
			log.info("execute " + this.getClass() + " state.");
			fsm.changeState(SearchState.class);
		}

		@Override
		public void onExit() {
			log.info("exit " + this.getClass() + " state.");
		}
	}
	public static class SearchState extends IdleState {
		@Override
		public void execute() {
			log.info("execute " + this.getClass() + " state.");
			fsm.changeState(TrackState.class);
		}
		
	}

	public static class TrackState extends IdleState {
		@Override
		public void execute() {
			log.info("execute " + this.getClass() + " state.");
			fsm.changeState(AttackState.class);
		}}

	public static class AttackState extends IdleState {
		@Override
		public void execute() {
			log.info("execute " + this.getClass() + " state.");
			fsm.changeState(GoHomeState.class);
		}}
	
	public static class GoHomeState extends IdleState {
		@Override
		public void execute() {
			log.info("execute " + this.getClass() + " state.");
		}}

	public MonsterAI(BaseState defaultState, BaseState... otherStates) {
		super(defaultState, otherStates);
	}
	
	public static void main(String[] args) throws Exception {
		MonsterAI ai = new MonsterAI(new IdleState(), new SearchState(), new TrackState(), new AttackState(), new GoHomeState());
		ai.start();
		
		Thread.sleep(3000l);
		
		ai.stop();
	}

}
