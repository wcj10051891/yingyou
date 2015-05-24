package pftest;

import java.util.Arrays;

public class TestArraySort {
	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Object[] o = new Object[]{4,5,6,7,2,3,1};
		System.out.println(Arrays.toString(o));
		Arrays.sort(o);
		System.out.println(Arrays.toString(o));
	}
}
