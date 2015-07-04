package test;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TimeTest {

	public static void main(String[] args) throws Exception {
//		Calendar c = Calendar.getInstance();
//		c.set(2035, 11, 31, 23, 59, 59);//1483199999,1451577599717
//		System.out.println(c.getTime());
//		System.out.println(c.getTimeInMillis());
//		System.out.println(System.currentTimeMillis());
//		
//		int i = 2040582399 + 200000000;
//		System.out.println(i);
//		
//		System.out.println(Long.valueOf(System.currentTimeMillis() / 1000l).intValue());
//		JSONArray a = new JSONArray();
//		JSONObject o = new JSONObject();
//		o.put("id", 1);
//		o.put("lv", 1);
//		a.add(o);
//		System.out.println(a.toString());
		
//		System.out.println(System.getProperty("java.io.tmpdir"));
		
		JarFile jar = new JarFile("C:/Users/Administrator/Desktop/core.jar");
		Enumeration<JarEntry> entries = jar.entries();
		while(entries.hasMoreElements()) {
			JarEntry e = entries.nextElement();
			System.out.println(e.getName() + ":" + e.getSize());
		}
		System.out.println(jar);
	}

}
