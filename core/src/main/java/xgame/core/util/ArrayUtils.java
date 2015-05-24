package xgame.core.util;

public abstract class ArrayUtils {
	public static <T> void each(T[] array, Visitor<T> vistor) {
		for(T object : array) {
			vistor.visit(object);
		}
	}
	
	public static interface Visitor<T> {
		void visit(T object);
	}
}
