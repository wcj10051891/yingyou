package com.shadowgame.rpg.modules.skill;

import java.util.concurrent.Callable;

import com.shadowgame.rpg.modules.core.Fighter;

/**
 * 技能施放命令
 * 
 * @author wcj10051891@gmail.com
 */
public class AttackCommand implements Callable<Object> {
	public Fighter source;
	public AbstractSkill skill;
	public Fighter target;

	public AttackCommand(Fighter source, AbstractSkill skill, Fighter target) {
		this.source = source;
		this.skill = skill;
		this.target = target;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object call() throws Exception {
		return skill.use(source, target);
	}
}
