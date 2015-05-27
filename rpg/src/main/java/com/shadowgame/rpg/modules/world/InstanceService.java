package com.shadowgame.rpg.modules.world;

import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.core.VisibleObject;
import com.shadowgame.rpg.service.Services;

public class InstanceService {
	public synchronized WorldMapInstance getNextAvailableInstance(int worldId,
			int destroySecond) {
		WorldMap map = Services.appService.world.getWorldMap(worldId);

		if (!map.isInstanceType())
			throw new AppException("Invalid call for next available instance  of " + worldId);

		int nextInstanceId = map.getNextInstanceId();

		WorldMapInstance worldMapInstance = new WorldMapInstance(map,
				nextInstanceId);
		map.addInstance(nextInstanceId, worldMapInstance);
		// 刷怪
		// spawnEngine.spawnInstance(worldId, worldMapInstance.getInstanceId());
		// 副本销毁倒计时
		if (destroySecond == 0)
			destroySecond = 60 * 30;// TODO take from template

		setDestroyTime(worldMapInstance, destroySecond);
		return worldMapInstance;
	}

	/**
	 * 设置副本销毁
	 */
	private void setDestroyTime(final WorldMapInstance instance, int destroySecond) {
		Future<?> destroyTask = instance.getDestroyTask();
		if (destroyTask != null)
			destroyTask.cancel(true);

		destroyTask = Services.timerService.jdkScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				destroyInstance(instance);
			}
		}, destroySecond, TimeUnit.SECONDS);
		instance.setDestroyTask(destroyTask);
	}
	
	public void destroyInstance(WorldMapInstance instance) {
		int mapId = instance.getMapId();
		int instanceId = instance.getInstanceId();

		WorldMap map = Services.appService.world.getWorldMap(mapId);
		map.removeWorldMapInstance(instanceId);

		for(Iterator<VisibleObject> it = instance.objectIterator();it.hasNext();) {
			VisibleObject obj = it.next();
			if(obj instanceof Player) {			
				Player player = (Player) obj;
//				moveToEntryPoint((Player) obj, portal, true);
			} else {
//				obj.getController().delete();
			}
				
		}
	}

}
