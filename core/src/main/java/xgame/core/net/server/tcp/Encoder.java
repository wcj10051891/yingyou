package xgame.core.net.server.tcp;

import org.jboss.netty.buffer.ChannelBuffer;

public interface Encoder {
	ChannelBuffer encode(Object message);
}
