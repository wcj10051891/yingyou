package test;

import java.util.Calendar;

public class TimeTest {

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.set(2035, 11, 31, 23, 59, 59);//1483199999,1451577599717
		System.out.println(c.getTime());
		System.out.println(c.getTimeInMillis());
		System.out.println(System.currentTimeMillis());
		
		int i = 2040582399 + 200000000;
		System.out.println(i);
		
		System.out.println(Long.valueOf(System.currentTimeMillis() / 1000l).intValue());
	}

}
