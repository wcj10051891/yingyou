package com.shadowgame.rpg.modules.skill;

import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.persist.entity.Skill;

/**
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractSkill {
	public Skill entity;
	public abstract Object use(AbstractFighter source, AbstractFighter target);
}
