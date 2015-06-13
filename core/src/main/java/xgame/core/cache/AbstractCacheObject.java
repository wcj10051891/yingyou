package xgame.core.cache;

import java.util.List;
import java.util.Map;

public abstract class AbstractCacheObject<K, E> implements CacheObject<K, E> {
	
	@Override
	public abstract E get(K key);

	@Override
	public Map<K, E> gets(List<K> keys) {
		return null;
	}
	
	@Override
	public abstract CacheObject<K, E> init(E entity, Object... contextParam);

	@Override
	public E create(Object... contextParam) {
		return null;
	}

	@Override
	public void update() {
	}

	@Override
	public void delete() {
	}
	
	@Override
	public abstract K getKey();
}
