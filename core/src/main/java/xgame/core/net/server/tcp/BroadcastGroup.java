package xgame.core.net.server.tcp;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BroadcastGroup {
	private String name;
	private ConcurrentLinkedQueue<Integer> channelIds;

	public BroadcastGroup(String name) {
		this.name = name;
		this.channelIds = new ConcurrentLinkedQueue<Integer>();
	}

	public BroadcastGroup(String name, Integer... channelIds) {
		this.name = name;
		this.channelIds = new ConcurrentLinkedQueue<Integer>();
		for (Integer channelId : channelIds)
			this.channelIds.add(channelId);
	}

	public String getName() {
		return this.name;
	}

	public void add(Integer channelId) {
		this.channelIds.add(channelId);
	}

	public void remove(Integer channelId) {
		this.channelIds.remove(channelId);
	}

	public Collection<Integer> getChannelIds() {
		return this.channelIds;
	}

	@Override
	public String toString() {
		return "channel group:" + name + " -> " + channelIds;
	}
}
