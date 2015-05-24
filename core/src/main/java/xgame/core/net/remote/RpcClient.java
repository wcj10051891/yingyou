package xgame.core.net.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.phprpc.PHPRPC_Callback;
import org.phprpc.PHPRPC_Client;
import org.phprpc.PHPRPC_Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient
{
	private static final Logger log = LoggerFactory.getLogger(RpcClient.class);
    private static final int RETRY_TIMES = 3;
    private final Map<String, PHPRPC_Client> clientMap = new ConcurrentHashMap<String, PHPRPC_Client>();
    private final Set<CallBackInternal> callBackSet = new CopyOnWriteArraySet<CallBackInternal>();
    private static final String Private_Key = "test";
    
    private PHPRPC_Client getClient(String server, String remoteClass)
    {
        String name = server + "_" + remoteClass;
        PHPRPC_Client client = clientMap.get(name);
        if (client == null)
        {
            String url = "http://" + server + "/rpc/" + remoteClass;
            client = new PHPRPC_Client(url);
            //client.setCharset("iso8859-1");
            client.setPrivateKey(Private_Key);
            clientMap.put(server, client);
        }
        return client;
    }

    /**
     * 直接调用远程方法rpc
     * @param server              	rpc服务的ip冒号端口，80端口可省略
     * @param remoteClass         	远程对象名
     * @param methodName			方法名
     * @param args                	参数数组
     * @return Object
     */
    public Object callDirect(String server, String remoteClass, String methodName, Object[] args)
    {
        server = server.toLowerCase();
        remoteClass = remoteClass.toLowerCase();
        methodName = methodName.toLowerCase();
        PHPRPC_Client client = getClient(server, remoteClass);
        log.info("RpcService.callDirect client={}, server={}, remoteClass={}, method={}, args={}", client, server, remoteClass, methodName, args);
        Object ret = client.invoke(methodName, args);
        //error
        if (ret instanceof PHPRPC_Error)
            throw new RuntimeException("RPC error", (PHPRPC_Error)ret);
        //warning
        if (client.getWarning() != null)
            throw new RuntimeException("RPC error", client.getWarning());
        log.info("RpcService.callDirect success. return:{}", ret);
        return ret;
    }

    /**
     * 异步调用远程方法rpc
     * @param server              	rpc服务的ip冒号端口
     * @param remoteClass         	远程对象名
     * @param callback        		回调对象
     * @param methodName     		方法名
     * @param args                	参数数组
     */
    public void callAsync(String server, String remoteClass, String methodName, Object[] args, CallBack callback)
    {
        PHPRPC_Client client = getClient(server, remoteClass);
        CallBackInternal callBack = new CallBackInternal(client, callback, methodName, args);
        client.invoke(methodName, args, callBack);
        callBackSet.add(callBack);
        log.info("RpcService.callAsync client={}, server={}, remoteClass={}, method={}, args={}", client, server, remoteClass, methodName, args);
    }

    public String dump()
    {
        StringBuilder dump = new StringBuilder();
        //dump.append("RPC url: ").append(url).append("\n");
        synchronized(callBackSet)
        {
            for(CallBackInternal callBack : callBackSet)
                dumpOne(dump, callBack.methodName, callBack.args).append("\n");
        }
        dump.delete(dump.length()-1, dump.length());
        return dump.toString();
    }

    private StringBuilder dumpOne(StringBuilder dump, String methodName, Object[] args)
    {
        dump.append(methodName).append("(");
        for(Object arg : args)
            dump.append(arg).append(", ");
        dump.delete(dump.length()-2, dump.length());
        dump.append(")");
        return dump;
    }

    public static byte[] toBytes(Object obj)
    {
        try
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(data);
            out.writeObject(obj);
            out.flush();
            out.close();
            return data.toByteArray();
        }
        catch (IOException e)
        {
            throw new RuntimeException("serialize error!", e);
        }
    }

    public static Object fromBytes(byte[] bytes)
    {
        try
        {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return in.readObject();
        }
        catch (Exception e)
        {
            log.error("unserialize error.", e);
            throw new RuntimeException("unserialize error!");
        }
    }

    //phprpc 会把byte[]用utf8编码成String传递，我们要自己在这里编码成iso8859-1才能正确的传递byte[]
    public static String toString(Object obj) throws IOException
    {
        return new String(toBytes(obj), "iso8859-1");
    }

    public static Object fromString(String str) throws Exception
    {
        return fromBytes(str.getBytes("iso8859-1"));
    }


    private class CallBackInternal extends PHPRPC_Callback
    {
        final PHPRPC_Client client;
        final CallBack callBack;
        final String methodName;
        final Object[] args;
        int retry = 0;

        CallBackInternal(PHPRPC_Client client, CallBack callBack, String methodName, Object[] args)
        {
            this.client = client;
            this.callBack = callBack;
            this.methodName = methodName;
            this.args = args;
        }

        public void handle(Object result, Object[] args, String output, PHPRPC_Error warning)
        {
            if (warning != null)
            {
                StringBuilder dump = new StringBuilder();
                dumpOne(dump, methodName, args);
                dump.append(" result: ").append(result);
                dump.append(" output: ").append(output);
                dump.append(" warning: ").append(warning);
                log.warn(dump.toString(), warning);
                errorHandler(warning);
            }
            else
            {
                callBackSet.remove(CallBackInternal.this);
                if (callBack != null)
                    callBack.callBack(result);
                log.info("RpcService.callAsync success. return={}", result);
            }
        }

        public void errorHandler(Throwable error)
        {
            if (retry++ < RETRY_TIMES)
                client.invoke(methodName, args, CallBackInternal.this);
            else
            {
                StringBuilder dump = new StringBuilder();
                dumpOne(dump, methodName, args);
                dump.append(" throws error: ").append(error);
                callBackSet.remove(CallBackInternal.this);
            }
        }
    }

    public interface CallBack
    {
        void callBack(Object output);
    }
    
    public static void main(String[] args) {
//    	RpcClient rc = new RpcClient();
//    	System.out.println(rc.callDirect("localhost:8181", "hotswaper", "redefineClass", new Object[]{IOUtils.read("src/com/wabao.mogame/remote/phprpc/Status.java")}));
//    	System.out.println(rc.callDirect("localhost:8181", "status", "online", null));
//    	System.out.println(rc.callDirect("localhost:8181", "hotswaper", "addJarFile", new Object[]{"C:/Users/wcj/Desktop/excel2db.jar"}));
//    	rc.callAsync("localhost:8181", "status", "online", null, new CallBack() {
//			@Override
//			public void callBack(Object output) {
//				System.out.println(output);
//			}
//		});
    }

}
