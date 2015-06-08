package test;

import org.phprpc.PHPRPC_Client;

public class PhpRpcTest {
	public static void main(String[] args) {
		PHPRPC_Client c = new PHPRPC_Client("http://124.202.153.51:8181/server/rpc/status");
		System.out.println(c.invoke("online", null));
	}
}
