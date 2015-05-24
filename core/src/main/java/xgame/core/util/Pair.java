package xgame.core.util;

public class Pair<K, V> {
	private K key;
	private V value;

	public Pair(K key, V value) {
		set(key, value);
	}

	public K key() {
		return key;
	}

	public V value() {
		return value;
	}

	public void set(K key, V value) {
		this.key = key;
		this.value = value;
	}
}
