package xgame.core.net.server.http;

import static org.jboss.netty.channel.Channels.pipeline;

import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

/**
 * User: kofboy@163.com
 * Date: 2010-4-26
 */
public class ServletPipelineFactory implements ChannelPipelineFactory
{
    private final NettyHttpServer server;
    private final ExecutionHandler executionHandler;

    public ServletPipelineFactory(NettyHttpServer server)
    {
        this.server = server;
//        this.executionHandler = new ExecutionHandler(new MemoryAwareThreadPoolExecutor(128, 0, Runtime.getRuntime().maxMemory()/8));
        this.executionHandler = new ExecutionHandler(Executors.newCachedThreadPool());
    }

    public ChannelPipeline getPipeline() throws Exception
    {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        //pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("executionHandler", executionHandler);//在新线程处理后面的handler
        pipeline.addLast("handler", new ServletHandler(server));
        return pipeline;
    }

}
