package xgame.client;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.ClassUtils;

import com.shadowgame.rpg.net.msg.Message;

public class BinaryMsgService extends com.shadowgame.rpg.net.msg.BinaryMsgService {
	private static final Logger log = LoggerFactory.getLogger(BinaryMsgService.class);
	private static final Pattern msgClassPattern = Pattern.compile("\\w+_(\\d+)");
	private Map<Integer, Class<?>> Sc_id2msg = new HashMap<Integer, Class<?>>();
	public Map<Class<?>, Integer> Cs_msg2Id = new HashMap<Class<?>, Integer>();
	
	public void start() {
		for (Class<?> clazz : ClassUtils.scanPackage("com.shadowgame.rpg.msg")) {
			if(Message.class.isAssignableFrom(clazz)) {
				String clsName = clazz.getSimpleName();
				Matcher m = msgClassPattern.matcher(clsName);
				if(m.matches()) {
					int id = Integer.valueOf(m.group(1));
					if(clsName.startsWith("Cs"))
						Cs_msg2Id.put(clazz, id);
					else if(clsName.startsWith("Sc"))
						Sc_id2msg.put(id, clazz);
					log.info("find msg {}->{}", id, clazz);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Message decode(int msgId, ChannelBuffer buf) throws Exception {
		return decodeMessageType((Class<Message>)Sc_id2msg.get(msgId), buf);
	}
}
