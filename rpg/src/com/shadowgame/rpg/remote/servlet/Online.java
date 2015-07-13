package com.shadowgame.rpg.remote.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.shadowgame.rpg.data.SkillData;
import com.shadowgame.rpg.modules.monster.Monster;
import com.shadowgame.rpg.modules.skill.AbstractSkill;
import com.shadowgame.rpg.persist.entity.TSkill;
import com.shadowgame.rpg.service.Services;

@WebServlet
public class Online extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
////			Field f = CacheService.class.getDeclaredField("softCache");
////			f.setAccessible(true);
////			ConcurrentHashMap<String, WeakReference<CacheObject>> c = (ConcurrentHashMap<String, WeakReference<CacheObject>>)f.get(Services.cacheService);
////			for (Entry<String, WeakReference<CacheObject>> e : c.entrySet()) {
////				System.out.println(e.getKey() + " -> " + e.getValue());
////			}
////
//////			Field f2 = CacheService.class.getDeclaredField("refQueue");
//////			f2.setAccessible(true);
////			Method m = CacheService.class.getDeclaredMethod("removeStaleEntries");
////			m.setAccessible(true);
////			m.invoke(Services.cacheService);
////			
//////			Player player = Services.cacheService.get(4676265064748060672l, Player.class, true);
//////			player.channel = null;
//////			player.daily = null;
//////			player.extAttribute = null;
//////			player.knapsack = null;
//////			player.processQueue = null;
//////			player.eventManager = null;
//////			player.missionManager = null;
//////			player.skillList = null;
//			
////			Services.config.start();
////			Player player = Services.app.world.allPlayers.getOnlinePlayer(4676265064748060672l);
////			player.buffList.validate();
////			System.out.println(player.buffList.haveBuff(1));
			
//			Map<Integer, Monster> os = Services.app.world.getWorldMap(1).getDefaultInstance().getMapObjectByType(Monster.class);
//			Monster monster = os.get(1);
//			System.out.println(monster.buffList.getBuffs());
			
//			Map<Long, Monster> m = Services.app.world.getWorldMap(1).getDefaultInstance().getMapObjectByType(Monster.class);
//			Monster monster = m.values().iterator().next();
//			System.out.println(monster);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
