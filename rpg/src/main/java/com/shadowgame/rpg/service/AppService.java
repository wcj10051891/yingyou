package com.shadowgame.rpg.service;

import java.lang.instrument.Instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.remote.RpcClient;
import xgame.core.util.InstrumentAgent;
import xgame.core.util.Service;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.Dispatcher;
import com.shadowgame.rpg.modules.world.World;

public class AppService implements Service {
	private static final Logger log = LoggerFactory.getLogger(AppService.class);
	public Instrumentation instrument;
	public RpcClient rpcClient;
	public Dispatcher dispatcher;
	public World world;

	@Override
	public void start() throws Exception {
		instrument = InstrumentAgent.instrument;
		if(instrument == null)
			throw new AppException("instrument init error.");
		
		rpcClient = new RpcClient();
		dispatcher = new Dispatcher();
		world = new World();
	}

	@Override
	public void stop() throws Exception {
	}
}
