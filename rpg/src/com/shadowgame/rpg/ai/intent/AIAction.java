package com.shadowgame.rpg.ai.intent;

import com.shadowgame.rpg.ai.AbstractAI;


/**
 * 有AI的对象进行AI思考之后的行为，追击、攻击、逃走、回刷新点等
 * @author wcj10051891@gmail.com
 * @Date 2015年6月8日 下午4:46:03
 */
public abstract class AIAction implements Comparable<AIAction>{
	/**
	 * 行为的迫切程度
	 */
	protected int power;
	
	public AIAction(int power) {
		this.power = power;
	}
	
	/**
	 * 行为逻辑执行
	 * @param ai
	 * @return 返回是否下次继续执行，如果不执行了，会从行为列表中移除
	 */
	public abstract boolean execute(AbstractAI ai);
	/**
	 * 当从intent列表中移除，结束生命周期时候执行
	 */
	public void onRemove(AbstractAI ai) {
		
	}

	@Override
	public int compareTo(AIAction o) {
		return o.getPower() - getPower();
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	public void increasePower(int power) {
		this.power += power;
	}
	
	public void decreasePower(int power) {
		this.power -= power;
	}
}
