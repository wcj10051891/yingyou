package com.shadowgame.rpg.modules.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.msg.skill_13.AttackInfo;
import com.shadowgame.rpg.msg.skill_13.Sc_13001;
import com.shadowgame.rpg.util.MapUtil;

/**
 * 抽象技能
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractSkill implements SkillLogic {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractSkill.class);

	@Override
	public long getDelay(SkillTask task) {
		return task.fighterSkill.entity.delay;
	}

	@Override
	public long getPeroid(SkillTask task) {
		return task.fighterSkill.entity.period;
	}

	@Override
	public void beforeExecute(SkillTask task) {
		boolean hit = SkillUtils.isHit(task.attacker, task.fighterSkill, task.target);
		log.info("{} use skill {} to {}, before execute, hit:{}", task.attacker, task.fighterSkill, task.target, hit);
		if(!hit)
			throw new NoticeException("未命中");
	}

	@Override
	public void execute(SkillTask task) {
		List<AbstractFighter> targetFighters = getTargetFighters(task, new HashSet<AbstractFighter>(Arrays.asList(task.attacker)));
		if(targetFighters.isEmpty()) {
			log.info("{} use skill {} to {}, execute result:no target find", task.attacker, task.fighterSkill, task.target);
		} else {
			List<AttackInfo> attackInfos = new ArrayList<AttackInfo>();
			for (AbstractFighter f : targetFighters) {
				Object[] attack = SkillUtils.attack(task.attacker, task.fighterSkill, f);
				log.info("{} use skill {} to {}, execute result:{}", task.attacker, task.fighterSkill, f, Arrays.toString(attack));
				AttackInfo a = new AttackInfo();
				a.targetId = f.getObjectId();
				a.damage = (Integer)attack[0];
				attackInfos.add(a);
			}
			Sc_13001 result = new Sc_13001();
			result.attackerId = task.attacker.getObjectId();
			result.attackInfos = attackInfos;
		}
	}

	@Override
	public void afterExecute(SkillTask task) {
	}

	@Override
	public boolean isFinish(SkillTask task) {
		if(task.executeCount >= task.fighterSkill.entity.count)
			return true;
		return false;
	}
	
	@Override
	public List<AbstractFighter> getTargetFighters(SkillTask task, Collection<AbstractFighter> excludes) {
		if(excludes == null || excludes.isEmpty())
			excludes = Collections.emptySet();
		List<AbstractFighter> result = new ArrayList<AbstractFighter>();
		if(task.fighterSkill.entity.rangeType == 3) {//扇形
			Position srcPosition = task.attacker.getPosition();
			Point sectorStartPoint = null;
			Point sectorEndPoint = null;
			if(task.fighterSkill.entity.rangePosition == 1) {
				sectorStartPoint = srcPosition.getPoint();
				sectorEndPoint = task.targetPoint;
			} else {
				sectorStartPoint = task.targetPoint;
				sectorEndPoint = srcPosition.getPoint();
			}
			double ra = Math.atan2(sectorEndPoint.y - sectorStartPoint.y, sectorEndPoint.x - sectorStartPoint.x);
			double half = Math.toRadians(task.fighterSkill.entity.rangeAngle) / 2;
			double startAngle = ra - half;
			double endAngle = ra + half;
			double pi2 = 2 * Math.PI;
			for (AbstractFighter f : srcPosition.getMapInstance().getFightersByRadius(sectorStartPoint.x, sectorStartPoint.y, task.fighterSkill.entity.rangeDistance)) {
				if(excludes.contains(f))
					continue;
				Point target = f.getPosition().getPoint();
				if(MapUtil.calcDistance(sectorStartPoint, target) > task.fighterSkill.entity.rangeDistance)
					continue;
				double a2 = Math.atan2(target.y - sectorStartPoint.y, target.x - sectorStartPoint.x);
				if((a2 > startAngle && a2 < endAngle) ||
						((a2 - pi2) > startAngle && (a2 - pi2) < endAngle) ||
						((a2 + pi2) > startAngle && (a2 + pi2) < endAngle)) {
					result.add(f);
				}
			}
		} else if(task.fighterSkill.entity.rangeType == 2) {//圆形
			Position srcPosition = task.attacker.getPosition();
			Point startPoint = task.fighterSkill.entity.rangePosition == 1 ? srcPosition.getPoint() : task.targetPoint;
			for (AbstractFighter f : srcPosition.getMapInstance().getFightersByRadius(startPoint.x, startPoint.y, task.fighterSkill.entity.rangeDistance)) {
				if(excludes.contains(f))
					continue;
				Point targetPoint = f.getPosition().getPoint();
				int _x = startPoint.x - targetPoint.x;
				int _y = startPoint.y - targetPoint.y;
				if(_x * _x + _y * _y <= task.fighterSkill.entity.rangeDistance * task.fighterSkill.entity.rangeDistance)
					result.add(f);
			}
			
		} else if(task.fighterSkill.entity.rangeType == 1) {//矩形
			Position srcPosition = task.attacker.getPosition();
			Point startPoint = task.fighterSkill.entity.rangePosition == 1 ? srcPosition.getPoint() : task.targetPoint;
			int half = task.fighterSkill.entity.rangeWidth / 2;
			double sin45 = Math.sin(Math.PI / 4);
			for (AbstractFighter f : srcPosition.getMapInstance().getFightersByRadius(startPoint.x, startPoint.y, task.fighterSkill.entity.rangeDistance)) {
				if(excludes.contains(f))
					continue;
				int x = f.getPosition().getX() - startPoint.x;
				int y = -(f.getPosition().getY() - startPoint.y);
				switch (task.direction) {
					case 0:
						if (y >= 0 && y <= task.fighterSkill.entity.rangeDistance && Math.abs(x) <= half)
							result.add(f);
						break;
					case 1:
						if (y + x >= 0 && y + x <= task.fighterSkill.entity.rangeDistance / sin45 && Math.abs(y - x) <= half / sin45)
							result.add(f);
						break;
					case 2:
						if (x >= 0 && x <= task.fighterSkill.entity.rangeDistance && Math.abs(y) <= half)
							result.add(f);
						break;
					case 3:
						if (y - x <= 0 && y - x >= -task.fighterSkill.entity.rangeDistance / sin45 && Math.abs(y + x) <= half / sin45)
							result.add(f);
						break;
					case 4:
						if (y <= 0 && y >= -task.fighterSkill.entity.rangeDistance && Math.abs(x) <= half)
							result.add(f);
						break;
					case 5:
						if (y + x <= 0 && y + x >= -task.fighterSkill.entity.rangeDistance / sin45 && Math.abs(y - x) <= half / sin45)
							result.add(f);
						break;
					case 6:
						if (x <= 0 && x >= -task.fighterSkill.entity.rangeDistance && Math.abs(y) <= half)
							result.add(f);
						break;
					case 7:
						if (y - x >= 0 && y - x <= task.fighterSkill.entity.rangeDistance / sin45 && Math.abs(y + x) <= half / sin45)
							result.add(f);
						break;
					default:
						break;
				}
			}
		}
		return result;
	}
}