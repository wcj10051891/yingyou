package com.shadowgame.rpg.modules.core;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.MathUtil;

public class MoveManager {
	private AbstractSpirit spirit;
	private Future<?> moveTask;
	
	public MoveManager(AbstractSpirit spirit) {
		this.spirit = spirit;
	}
	
	public void startMove() {
		//检查是否可移动，不能移动则停止移动
		List<Point> movePath = this.spirit.getMovePath();
		if(movePath == null || movePath.isEmpty()) {
			this.stop();
			this.spirit.stopMoving();
			return;
		}
		final Point next = movePath.remove(0);
		double distance = MathUtil.getDistance(this.spirit.getPosition().getPoint(), next);
		long costMills = Double.valueOf(distance / this.spirit.getSpeed() * 1000l).longValue();
		moveTask = Services.timerService.jdkScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				Services.app.world.updatePosition(spirit, next);
				startMove();
			}
		}, costMills, TimeUnit.MILLISECONDS);
	}
	
	public boolean isMoving() {
		return this.moveTask != null && !this.moveTask.isCancelled();
	}
	
	public void stop() {
		if(moveTask != null) {
			moveTask.cancel(true);
			moveTask = null;
		}
	}
	
}
