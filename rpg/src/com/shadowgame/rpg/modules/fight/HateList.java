package com.shadowgame.rpg.modules.fight;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HateList {
	private Map<AbstractFighter, Integer> hates = new HashMap<AbstractFighter, Integer>(1);
	private AbstractFighter fighter;

	public HateList(AbstractFighter fighter) {
		super();
		this.fighter = fighter;
	}

	public AbstractFighter getOwner() {
		return fighter;
	}
	
	public void addHate(AbstractFighter attacker, int damage) {
		Integer v = hates.get(attacker);
		if(v == null)
			hates.put(attacker, damage);
		else
			hates.put(attacker, v + damage);
	}
	
	public void removeHate(AbstractFighter attacker) {
		if(attacker != null)
			hates.remove(attacker);
	}
	
	public void clear() {
		this.hates.clear();
	}
	
	public AbstractFighter getMostHate() {
		AbstractFighter result = null;
		int max = 0;
		for (Entry<AbstractFighter, Integer> e : this.hates.entrySet()) {
			if(e.getValue() > max) {
				max = e.getValue();
				result = e.getKey();
			}
		}
		return result;
	}
}
