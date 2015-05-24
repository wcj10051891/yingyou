package xgame.core.net.server.http;

import org.jboss.netty.handler.codec.http.DefaultHttpMessage;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * User: kofboy@163.com
 * Date: 2010-4-27
 */
/**
 * The default {@link org.jboss.netty.handler.codec.http.HttpResponse} implementation.
 *
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 * @version $Rev: 1685 $, $Date: 2009-08-28 16:15:49 +0900 (금, 28 8 2009) $
 */
public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse
{
    private HttpResponseStatus status;

    /**
     * Creates a new instance.
     *
     * @param version the HTTP version of this response
     * @param status  the status of this response
     */
    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status) {
        super(version);
        if (status == null) {
            throw new NullPointerException("status");
        }
        this.status = status;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        if (status == null) {
            throw new NullPointerException("status");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return getProtocolVersion().getText() + ' ' + getStatus().toString();
    }
}
