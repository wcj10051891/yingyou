package com.shadowgame.rpg.modules.fight;

import java.util.concurrent.locks.ReentrantLock;

public class LifeAttrs {
	protected int hp;
	protected int mp;
	protected boolean isDie;
	protected AbstractFighter owner;
	private ReentrantLock hpLock = new ReentrantLock();
	private ReentrantLock mpLock = new ReentrantLock();

	public LifeAttrs(AbstractFighter owner, int hp, int mp) {
		super();
		this.owner = owner;
		this.hp = hp;
		this.mp = mp;
	}

	public int getHp() {
		return hp;
	}

	public int getMaxHp() {
		return this.owner.fightAttrs.get(AttrType.maxHp);
	}

	public int getMp() {
		return mp;
	}

	public int getMaxMp() {
		return this.owner.fightAttrs.get(AttrType.maxMp);
	}

	public boolean isDie() {
		return isDie;
	}

	public AbstractFighter getOwner() {
		return owner;
	}

	public int reduceHp(int value, AbstractFighter attacker) {
		hpLock.lock();
		try {
			int newHp = this.hp - value;
			if (newHp < 0) {
				newHp = 0;
				if (!isDie) {
					isDie = true;
				}
			}
			this.hp = newHp;
			onReduceHp();
			if (isDie) {
				this.owner.onDie(attacker);
			}
		} finally {
			hpLock.unlock();
		}
		return hp;
	}

	public int reduceMp(int value) {
		mpLock.lock();
		try {
			int newMp = this.mp - value;
			if (newMp < 0)
				newMp = 0;

			this.mp = newMp;
		} finally {
			mpLock.unlock();
		}
		onReduceMp();
		return mp;
	}

	public int increaseHp(int value) {
		if (value == getMaxHp())
			return 0;

		hpLock.lock();
		try {
			if (isDie()) {
				return 0;
			}
			int newHp = this.hp + value;
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}
			this.hp = newHp;
			onIncreaseHp();
		} finally {
			hpLock.unlock();
		}
		return hp;
	}

	public int increaseMp(int value) {
		if (value == getMaxMp())
			return 0;

		mpLock.lock();
		try {
			if (isDie()) {
				return 0;
			}
			int newMp = this.mp + value;
			if (newMp > getMaxMp()) {
				newMp = getMaxMp();
			}
			this.mp = newMp;
		} finally {
			mpLock.unlock();
		}
		onIncreaseMp();
		return mp;
	}

	/**
	 * @param hp
	 */
	public void setCurrentHp(int value) {
		hpLock.lock();
		try {
			this.hp = value;
			if (this.hp > 0)
				this.isDie = false;
		} finally {
			hpLock.unlock();
		}
	}

	protected void onReduceHp() {

	}

	protected void onReduceMp() {
		
	}

	protected void onIncreaseHp() {

	}

	protected void onIncreaseMp() {

	}
}
