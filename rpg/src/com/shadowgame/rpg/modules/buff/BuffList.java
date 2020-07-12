package com.shadowgame.rpg.modules.buff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.shadowgame.rpg.modules.fight.AbstractFighter;

public class BuffList {
	protected AbstractFighter fighter;
	protected List<BuffTask> buffs; 

	public BuffList(AbstractFighter fighter) {
		this.fighter = fighter;
		this.buffs = new ArrayList<BuffTask>();
	}
	
	public void addBuff(BuffTask buff) {
		this.buffs.add(buff);
	}
	
	public void removeBuff(BuffTask buff) {
		this.buffs.remove(buff);
	}
	
	public void removeBuff(int buffId) {
		if(!this.buffs.isEmpty()) {
			for (Iterator<BuffTask> it = this.buffs.iterator(); it.hasNext();) {
				BuffTask buff = it.next();
				if(buff.buffLogic.getBuff().id == buffId)
					it.remove();
			}
		}
	}
	
	public boolean haveBuff(int buffId) {
		for (BuffTask buff : this.buffs) {
			if(buff.buffLogic.getBuff().id == buffId)
				return true;
		}
		return false;
	}
	
	public void validate() {
		if(!this.buffs.isEmpty()) {
			for (Object buff : this.buffs.toArray())
				((BuffTask)buff).checkFinish();
		}
	}

	public List<BuffTask> getBuffs() {
		return buffs;
	}
	
}
