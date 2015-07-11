package com.shadowgame.rpg.data;

import java.util.HashMap;
import java.util.Map;

import xgame.core.util.ObjectUtils;

import com.shadowgame.rpg.core.AbstractData;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.buff.AbstractBuff;
import com.shadowgame.rpg.persist.dao.TBuffDao;
import com.shadowgame.rpg.persist.entity.TBuff;
import com.shadowgame.rpg.service.Services;

public class BuffData extends AbstractData {
	private TBuffDao dao = Services.daoFactory.get(TBuffDao.class);
	public Map<Integer, TBuff> buffs = new EntityMap<Integer, TBuff>();
	public Map<Integer, AbstractBuff> buffLogics = new HashMap<Integer, AbstractBuff>();
	
	public void load() {
		for (TBuff entity : dao.getAll()) {
			buffs.put(entity.id, entity);
			try {
				Class<?> clazz = Class.forName("com.shadowgame.rpg.modules.buff.impl.Buff" + entity.id);
				buffLogics.put(entity.id, (AbstractBuff)ObjectUtils.create(clazz, new Class[]{TBuff.class}, new Object[]{entity}));
			} catch (Exception e) {
				throw new AppException("buff init error:" + entity.id, e);
			}
		}
	}
}
