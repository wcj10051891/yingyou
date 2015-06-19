package test;

import java.util.concurrent.TimeUnit;


public class Test {
	static class A implements Comparable<A>{
		int i;
		@Override
		public String toString() {
			return "A." + i;
		}

		@Override
		public int compareTo(A o) {
			return o.i - this.i;
		}
	}
	public static void main(String[] args) throws Exception {
//		A a = new A();
//		a.i = 1;
//		A b = new A();
//		b.i = 2;
//		
//		List<A> l = Arrays.asList(a, b);
//		Collections.sort(l);
//		System.out.println(l);
		
//		System.out.println("com.shadowgame.rpg.msg.login.Sc_10000".hashCode());
		
//		short s = (short) 47481;
//		System.out.println(s & 0xffff);

		long start = System.currentTimeMillis();
		Thread.sleep(1);
		System.out.println((System.currentTimeMillis() - start));
	}
}
