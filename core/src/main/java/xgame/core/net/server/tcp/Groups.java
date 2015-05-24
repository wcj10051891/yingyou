package xgame.core.net.server.tcp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Groups {
	private static final Logger log = LoggerFactory.getLogger(Groups.class);
	
	public static final String World = "world";
	
	private ConcurrentHashMap<String, BroadcastGroup> groups = new ConcurrentHashMap<String, BroadcastGroup>();

	private Channels channels;
	
	public Groups(Channels channels) {
		this.channels = channels;
	}
	
	public BroadcastGroup create(String groupName) {
		if(groups.containsKey(groupName))
			return groups.get(groupName);
		
		groups.putIfAbsent(groupName, new BroadcastGroup(groupName));
		return groups.get(groupName);
	}

	public void join(String groupName, Integer channelId) {
		BroadcastGroup group = groups.get(groupName);
		if (group == null) {
			groups.putIfAbsent(groupName, new BroadcastGroup(groupName, channelId));
		} else {
			group.add(channelId);
		}
	}

	public void leave(String groupName, Integer channelId) {
		BroadcastGroup group = groups.get(groupName);
		if (group != null)
			group.remove(channelId);
	}

	public BroadcastGroup get(String groupName) {
		return groups.get(groupName);
	}

	@SuppressWarnings("unchecked")
	public void broadcast(String groupName, Object message, Integer... excludeChannelIds) {
		BroadcastGroup group = groups.get(groupName);
		boolean noExclude = excludeChannelIds.length == 0;
		Set<Integer> excludeIds = noExclude ? Collections.EMPTY_SET : new HashSet<Integer>(Arrays.asList(excludeChannelIds));
		for (Integer channelId : group.getChannelIds()) {
			if (noExclude || !excludeIds.contains(channelId)) {
				Channel channel = channels.getChannel(channelId);
				if(channel != null)
					channel.write(message);
			}
		}
		log.info("broadcast to {}:{}, exclude:{}, group status:{}", groupName, message, Arrays.toString(excludeChannelIds), group.getChannelIds());
	}
}