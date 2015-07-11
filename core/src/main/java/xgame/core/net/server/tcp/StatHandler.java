package xgame.core.net.server.tcp;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

public class StatHandler extends SimpleChannelHandler {

	public long startTime = System.currentTimeMillis();
	public AtomicInteger connect = new AtomicInteger();
	public AtomicInteger in = new AtomicInteger();
	public AtomicInteger out = new AtomicInteger();
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		connect.incrementAndGet();
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		connect.decrementAndGet();
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.messageReceived(ctx, e);
		in.incrementAndGet();
	}
	
	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {
		super.writeComplete(ctx, e);
		out.incrementAndGet();
	}
}
