package com.shadowgame.rpg.modules.map;

import java.util.LinkedList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.MapUtil;

public class MoveManager {
	private AbstractSpirit spirit;
	private Future<?> moveTask;
	private LinkedList<Point> movePath;
	private Point dest;
	
	public MoveManager(AbstractSpirit spirit) {
		this.spirit = spirit;
	}
	
	public void start(Point newDest, LinkedList<Point> newPath) {
		this.dest = newDest;
		//检查是否可移动，不能移动则停止移动
		this.movePath = newPath;
		if(movePath == null || movePath.isEmpty()) {
			this.stop();
			this.spirit.stopMoving();
			return;
		}
		final Point next = movePath.poll();
		double distance = MapUtil.calcDistance(this.spirit.getPosition().getPoint(), next);
		long costMills = Double.valueOf(distance / this.spirit.getSpeed() * 1000l).longValue();
		moveTask = Services.timerService.jdkScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				Services.app.world.updatePosition(spirit, next.x, next.y);
				start(dest, movePath);
			}
		}, costMills, TimeUnit.MILLISECONDS);
	}
	
	public boolean isMoving() {
		return this.moveTask != null && !this.moveTask.isCancelled();
	}
	
	public boolean isMoveToDest(Point dest) {
		return isMoving() && this.dest.equals(dest);
	}

	public LinkedList<Point> getMovePath() {
		return movePath;
	}
	
	public void stop() {
		if(moveTask != null) {
			moveTask.cancel(true);
			moveTask = null;
		}
	}
	
}
