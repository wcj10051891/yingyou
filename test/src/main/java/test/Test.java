package test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	public static void main(String[] args) {
		A a = new A();
		a.i = 1;
		A b = new A();
		b.i = 2;
		
		List<A> l = Arrays.asList(a, b);
		Collections.sort(l);
		System.out.println(l);
	}
}
