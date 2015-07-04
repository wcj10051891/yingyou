package xgame.core.util;

public abstract class CommonUtils {

	public static <T> T nullToDef(T o, T def) {
		if (o == null)
			return def;
		return o;
	}
	
	public static Long nullLongToDef(Long o) {
		return nullToDef(o, 0l);
	}

	public static String nullStrToDef(String o) {
		return nullToDef(o, "");
	}

	public static Integer nullIntegerToDef(Integer o) {
		return nullToDef(o, 0);
	}

	public static Float nullFloatToDef(Float o) {
		return nullToDef(o, 0f);
	}

	public static Number nullNumberToDef(Number o) {
		return nullToDef(o, 0);
	}

	public static Boolean nullBooleanToDef(Boolean o) {
		return nullToDef(o, false);
	}

	public static int round(double d) {
		return Long.valueOf(Math.round(d)).intValue();
	}

	public static float round(float d) {
		return Math.round(d);
	}
}
