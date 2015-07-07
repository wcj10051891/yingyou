package com.shadowgame.rpg.modules.skill;

import java.util.ArrayList;
import java.util.List;

import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.persist.entity.TSkill;
import com.shadowgame.rpg.util.MapUtil;


/**
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractSkill implements SkillLogic {

	protected TSkill entity;
	
	public AbstractSkill(TSkill entity) {
		this.entity = entity;
	}
	
	public void fire(AbstractFighter attacker, AbstractFighter target) {
		new SkillTask(attacker, target, this).start();
	}

	@Override
	public long getDelay(SkillTask task) {
		return this.entity.delay;
	}

	@Override
	public long getPeroid(SkillTask task) {
		return this.entity.period;
	}

	@Override
	public void beforeExecute(SkillTask task) {
		System.out.println("before execute skill:" + this.entity.name);
	}

	@Override
	public void execute(SkillTask task) {
		System.out.println("execute skill:" + this.entity.name);
		
	}

	@Override
	public void afterExecute(SkillTask task) {
		System.out.println("after execute skill:" + this.entity.name);
	}

	@Override
	public boolean isFinish(SkillTask task) {
		if(task.executeCount >= this.entity.count)
			return true;
		return false;
	}
	
	public List<AbstractFighter> getTargetFighters(SkillTask task) {
		List<AbstractFighter> result = new ArrayList<AbstractFighter>();
		if(this.entity.rangeType == 3) {//扇形
			Position srcPosition = task.attacker.getPosition();
			Point sectorStartPoint = null;
			Point sectorEndPoint = null;
			if(this.entity.rangePosition == 1) {
				sectorStartPoint = srcPosition.getPoint();
				sectorEndPoint = task.targetPoint;
			} else {
				sectorStartPoint = task.targetPoint;
				sectorEndPoint = srcPosition.getPoint();
			}
			double ra = Math.atan2(sectorEndPoint.y - sectorStartPoint.y, sectorEndPoint.x - sectorStartPoint.x);
			double half = Math.toRadians(this.entity.rangeAngle) / 2;
			double startAngle = ra - half;
			double endAngle = ra + half;
			for (AbstractFighter f : srcPosition.getMapInstance().getFightersByRadius(sectorStartPoint.x, sectorStartPoint.y, this.entity.rangeDistance)) {
				if(isInSectorRange(startAngle, endAngle, this.entity.rangeDistance, sectorStartPoint, f.getPosition().getPoint()))
					result.add(f);
			}
			
		} else if(this.entity.rangeType == 2) {//圆形
			Position srcPosition = task.attacker.getPosition();
			Point startPoint = this.entity.rangePosition == 1 ? srcPosition.getPoint() : task.targetPoint;
			for (AbstractFighter f : srcPosition.getMapInstance().getFightersByRadius(startPoint.x, startPoint.y, this.entity.rangeDistance)) {
				Point targetPoint = f.getPosition().getPoint();
				int _x = startPoint.x - targetPoint.x;
				int _y = startPoint.y - targetPoint.y;
				if(_x * _x + _y * _y <= this.entity.rangeDistance * this.entity.rangeDistance)
					result.add(f);
			}
			
		} else if(this.entity.rangeType == 3) {//矩形
			Position srcPosition = task.attacker.getPosition();
			Point startPoint = this.entity.rangePosition == 1 ? srcPosition.getPoint() : task.targetPoint;
			int half = this.entity.rangeWidth / 2;
			double sin45 = Math.sin(Math.PI / 4);
			for (AbstractFighter f : srcPosition.getMapInstance().getFightersByRadius(startPoint.x, startPoint.y, this.entity.rangeDistance)) {
				int x = f.getPosition().getX() - startPoint.x;
				int y = -(f.getPosition().getY() - startPoint.y);
				switch (task.direction) {
					case 0:
						if (y >= 0 && y <= this.entity.rangeDistance && Math.abs(x) <= half)
							result.add(f);
						break;
					case 1:
						if (y + x >= 0 && y + x <= this.entity.rangeDistance / sin45 && Math.abs(y - x) <= half / sin45)
							result.add(f);
						break;
					case 2:
						if (x >= 0 && x <= this.entity.rangeDistance && Math.abs(y) <= half)
							result.add(f);
						break;
					case 3:
						if (y - x <= 0 && y - x >= -this.entity.rangeDistance / sin45 && Math.abs(y + x) <= half / sin45)
							result.add(f);
						break;
					case 4:
						if (y <= 0 && y >= -this.entity.rangeDistance && Math.abs(x) <= half)
							result.add(f);
						break;
					case 5:
						if (y + x <= 0 && y + x >= -this.entity.rangeDistance / sin45 && Math.abs(y - x) <= half / sin45)
							result.add(f);
						break;
					case 6:
						if (x <= 0 && x >= -this.entity.rangeDistance && Math.abs(y) <= half)
							result.add(f);
						break;
					case 7:
						if (y - x >= 0 && y - x <= this.entity.rangeDistance / sin45 && Math.abs(y + x) <= half / sin45)
							result.add(f);
						break;
					default:
						break;
				}
			}
		}
		return result;
	}
	
	private boolean isInSectorRange(double startAngle, double endAngle, int radius, Point source, Point target) {
		if(MapUtil.calcDistance(source, target) > radius)
			return false;
		double a2 = Math.atan2(target.y - source.y, target.x - source.x);
		double pi2 = 2 * Math.PI;
		if((a2 > startAngle && a2 < endAngle) ||
				((a2 - pi2) > startAngle && (a2 - pi2) < endAngle) ||
				((a2 + pi2) > startAngle && (a2 + pi2) < endAngle)) {
			return true;
		}
		return false;
	}
}
