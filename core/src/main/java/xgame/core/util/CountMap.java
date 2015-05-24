package xgame.core.util;

import java.util.HashMap;

/**
 * 将数字的和累计
 * @author chengjie.wang wcj10051891@gmail.com 2011-1-15 下午05:27:48
 */
public class CountMap<K, V extends Number> extends HashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7500928926137287488L;

	public CountMap() {
		super();
	}

	public CountMap(int capacity) {
		super(capacity);
	}

	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		if (value == null) {
			return super.put(key, value);
		}
		Number v = this.get(key);
		if (v != null) {
			if (v instanceof Byte) {
				v = value.byteValue() + v.byteValue();
			} else if (v instanceof Short) {
				v = value.shortValue() + v.shortValue();
			} else if (v instanceof Integer) {
				v = value.intValue() + v.intValue();
			} else if (v instanceof Long) {
				v = value.longValue() + v.longValue();
			} else if (v instanceof Float) {
				v = value.floatValue() + v.floatValue();
			} else if (v instanceof Double) {
				v = value.doubleValue() + v.doubleValue();
			}
			return super.put(key, (V) v);
		} else {
			return super.put(key, value);
		}
	}
}
