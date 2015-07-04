package com.shadowgame.rpg.data;

import java.util.HashMap;
import java.util.Map;

import xgame.core.util.ObjectUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.core.Data;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.exec.Exec;
import com.shadowgame.rpg.persist.dao.TExecutionDao;
import com.shadowgame.rpg.persist.entity.TExecution;
import com.shadowgame.rpg.service.Services;

@Data
public class ExecData {
	private TExecutionDao dao = Services.daoFactory.get(TExecutionDao.class);
	
	public Map<String, Exec> key2exec = new HashMap<>();
	private Map<String, Object> execImplObject = new HashMap<>();
	
	public ExecData() {
		for (TExecution entity : dao.getAll()) {
			execImplObject.put(entity.clazz, ObjectUtils.create(entity.clazz));
			this.key2exec.put(entity.key,
					new Exec(entity.key, execImplObject.get(entity.clazz), entity.method, JSONObject.parseObject(entity.param)));
		}
	}
	
	public boolean check(Player player, String str) {
		return (boolean)exec(player, str);
	}
	
	public <T> T exec(Player player, String str) {
		return new com.shadowgame.rpg.modules.exec.Execution(str).invoke(player);
	}
}
