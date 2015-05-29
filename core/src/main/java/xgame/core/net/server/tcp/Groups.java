package xgame.core.net.server.tcp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Groups {
	private static final Logger log = LoggerFactory.getLogger(Groups.class);
	
	public static final String World = "world";
	
	private ConcurrentHashMap<String, ChannelGroup> groups = new ConcurrentHashMap<String, ChannelGroup>();
	
	public ChannelGroup create(String groupName) {
		if(groups.containsKey(groupName))
			return groups.get(groupName);
		
		DefaultChannelGroup group = new DefaultChannelGroup(groupName);
		groups.put(groupName, group);
		return group;
	}

	public void join(String groupName, Channel channel) {
		create(groupName).add(channel);
	}

	public void leave(String groupName, Channel channel) {
		ChannelGroup group = groups.get(groupName);
		if (group != null)
			group.remove(channel);
	}

	public ChannelGroup get(String groupName) {
		return groups.get(groupName);
	}

	public void broadcast(ChannelBuffer message, String groupName, Channel... excludeChannels) {
		this.broadcast(message, groupName, new HashSet<>(Arrays.asList(excludeChannels)));
	}
	
	public void broadcast(ChannelBuffer message, String groupName, Set<Channel> excludeChannels) {
		ChannelGroup group = groups.get(groupName);
		if(group != null) {
			if(excludeChannels == null || excludeChannels.isEmpty()) {
				for (Channel channel : group)
					channel.write(message);
			} else {
				for (Channel channel : group) {
					if (!excludeChannels.contains(channel))
						channel.write(message);
				}
			}
		}
	}
}