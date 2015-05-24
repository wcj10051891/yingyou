package xgame.core.net.server.http;

import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * User: kofboy@163.com
 * Date: 2010-4-21
 */
public class NettyHttpSession implements HttpSession
{
    public static final String SESSION_ID_NAME = "JSESSIONID";
    private final String sessionId;
    private final long creationTime;
    private long accessTime;
    private int maxInactiveInterval;
    private boolean isNew = true;
    private Map<String, Object> attributeMap = new ConcurrentHashMap<String, Object>();

    public NettyHttpSession(int second)
    {
        accessTime = creationTime = System.currentTimeMillis();
        maxInactiveInterval = second * 1000;
        StringBuilder sb = new StringBuilder();
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(("fd35^&" + System.currentTimeMillis()).getBytes());
            for (byte b : md.digest())
                sb.append(Integer.toHexString(b & 0x000000FF | 0xFFFFFF00).substring(6));
        } catch(NoSuchAlgorithmException e){}
        sessionId = sb.toString();
    }

    boolean isActive()
    {
        long currentTime = System.currentTimeMillis();
        if (currentTime - accessTime > maxInactiveInterval)
            return false;
        isNew = false;
        accessTime = currentTime;
        return true;
    }



    //servlet api

    public long getCreationTime()
    {
        return creationTime;
    }

    public String getId()
    {
        return sessionId;
    }

    public long getLastAccessedTime()
    {
        return accessTime;
    }

    public ServletContext getServletContext() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public void setMaxInactiveInterval(int i)
    {
        maxInactiveInterval = i;
    }

    public int getMaxInactiveInterval()
    {
        return maxInactiveInterval;
    }

    /**
     * @deprecated Method getSessionContext is deprecated
     */
    public HttpSessionContext getSessionContext() { throw new UnsupportedOperationException("This method is not implemented!"); }

    public Object getAttribute(String name)
    {
        return attributeMap.get(name);
    }

    /**
     * @deprecated Method getValue is deprecated
     */
    public Object getValue(String name)
    {
        return getAttribute(name);
    }

    public Enumeration<String> getAttributeNames()
    {
        Vector<String> v = new Vector<String>();
        v.addAll(attributeMap.keySet());
        return v.elements();
    }

    /**
     * @deprecated Method getValueNames is deprecated
     */
    public String[] getValueNames()
    {
        return attributeMap.keySet().toArray(new String[attributeMap.size()]);
    }

    public void setAttribute(String name, Object obj)
    {
        attributeMap.put(name, obj);
    }

    /**
     * @deprecated Method putValue is deprecated
     */
    public void putValue(String name, Object obj)
    {
        setAttribute(name, obj);
    }

    public void removeAttribute(String name)
    {
        attributeMap.remove(name);
    }

    /**
     * @deprecated Method removeValue is deprecated
     */
    public void removeValue(String name) 
    {
        removeAttribute(name);
    }

    public void invalidate()
    {
        setMaxInactiveInterval(0);    
    }

    public boolean isNew()
    {
        return isNew;
    }

}
