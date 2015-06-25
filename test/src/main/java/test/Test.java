package test;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;



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

//		long start = System.currentTimeMillis();
//		Thread.sleep(1);
//		System.out.println((System.currentTimeMillis() - start));
		
		Set<Node> s = new TreeSet<Node>(new MyComparator());
		s.add(new Node(1));
		s.add(new Node(3));
		s.add(new Node(2));
		s.add(new Node(8));
		s.add(new Node(0));
		System.out.println(s);
	}
	
	static class MyComparator implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.i - o2.i;
		}
	}
	
	static class Node {
		int i;
		
		public Node(int i) {
			this.i = i;
		}
		
		@Override
		public boolean equals(Object obj) {
			return ((Node)obj).i == i;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + i;
			return result;
		}
		
		@Override
		public String toString() {
			return String.valueOf(i);
		}
	}
}
