package xgame.core.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MultiValueMap<K, V>
{
	private Map<K, Collection<V>> targetMap;
	private ValueCollectionCreator<V> valueCollectionCreator;
	
	public static interface ValueCollectionCreator<V>
	{
		Collection<V> createCollection();
	}
	
	public static interface TargetMapCreator<K, V>
	{
		Map<K, Collection<V>> createMap();
	}
	
	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}.
	 */
	public MultiValueMap()
	{
		this.targetMap = new LinkedHashMap<K, Collection<V>>();
		this.valueCollectionCreator = new ValueCollectionCreator<V>()
		{
			public Collection<V> createCollection()
			{
				return new LinkedList<V>();
			}
		};
	}

	public MultiValueMap(final Class<? extends Collection<V>> valueCollectionType)
	{
		this.targetMap = new LinkedHashMap<K, Collection<V>>();
		this.valueCollectionCreator = new ValueCollectionCreator<V>()
		{
			public Collection<V> createCollection()
			{
				return ObjectUtils.create(valueCollectionType, null, null);
			}
		};
	}
	
	public MultiValueMap(TargetMapCreator<K, V> mapCreator, ValueCollectionCreator<V> valueCollectionCreator)
	{
		this.targetMap = mapCreator.createMap();
		this.valueCollectionCreator = valueCollectionCreator;
	}
	
	public MultiValueMap(TargetMapCreator<K, V> mapCreator)
	{
		this.targetMap = mapCreator.createMap();
		this.valueCollectionCreator = new ValueCollectionCreator<V>()
		{
			public Collection<V> createCollection()
			{
				return new LinkedList<V>();
			}
		};
	}

	// MultiValueMap implementation

	public void add(K key, V value)
	{
		Collection<V> values = this.targetMap.get(key);
		if (values == null)
		{
			values = this.valueCollectionCreator.createCollection();
			this.targetMap.put(key, values);
		}
		values.add(value);
	}

	// Map implementation

	public int size()
	{
		return this.targetMap.size();
	}

	public boolean isEmpty()
	{
		return this.targetMap.isEmpty();
	}

	public boolean containsKey(Object key)
	{
		return this.targetMap.containsKey(key);
	}

	public boolean containsValue(Object value)
	{
		return this.targetMap.containsValue(value);
	}

	public Collection<V> get(Object key)
	{
		return this.targetMap.get(key);
	}

	public Collection<V> put(K key, Collection<V> value)
	{
		return this.targetMap.put(key, value);
	}

	public Collection<V> remove(Object key)
	{
		return this.targetMap.remove(key);
	}

	public void putAll(Map<? extends K, ? extends Collection<V>> m)
	{
		this.targetMap.putAll(m);
	}

	public void clear()
	{
		this.targetMap.clear();
	}

	public Set<K> keySet()
	{
		return this.targetMap.keySet();
	}

	public Collection<Collection<V>> values()
	{
		return this.targetMap.values();
	}

	public Set<Entry<K, Collection<V>>> entrySet()
	{
		return this.targetMap.entrySet();
	}

	@Override
	public boolean equals(Object obj)
	{
		return this.targetMap.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return this.targetMap.hashCode();
	}

	@Override
	public String toString()
	{
		return this.targetMap.toString();
	}
}
