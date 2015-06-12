package xgame.core.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Service;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CacheService implements Service {
	private static final Logger log = LoggerFactory.getLogger(CacheService.class);
    private ReferenceQueue<CacheObject> refQueue = new ReferenceQueue<CacheObject>();
	private ConcurrentHashMap<String, SoftReference<CacheObject>> softCache = new ConcurrentHashMap<String, SoftReference<CacheObject>>();
	private AtomicInteger putCount = new AtomicInteger();
	private static int removeQueuedSize = 1000;
	private Lock cacheLock = new ReentrantLock();
	private SaveQueue saveQueue;
	private ScheduledExecutorService scheduler;
	
	public CacheService(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}
	
	@Override
	public void start() throws Exception {
		saveQueue = new SaveQueue(this.scheduler);
	}
	
	@Override
	public void stop() throws Exception {
		saveQueue.saveOnStop();
	}
	
	public <T extends CacheObject, K> T get(K key, Class<T> objectClass, boolean loadFromDB, Object attachment) {
		if(key == null || key.toString().equals("0")) {
			log.info("get object invalid key:{}, class:{}, loadFromDB:{}.", 
				key, objectClass, loadFromDB);
			return null;
		}
		String k = k(key, objectClass);
		try {
			cacheLock.lock();
			SoftReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null) {
				T result = (T) ref.get();
				log.info("get object from cache, key:{}, class:{}, instance:{}, fromDB:{}.", 
						key, objectClass, result, loadFromDB);				
				return result;
			}
			
			if(!loadFromDB) {
				log.info("get object not in cache, nonCreate it. key:{}, class:{}, fromDB:{}.", 
						key, objectClass, loadFromDB);	
				return null;
			}
			
			try {
				T newObject = (T) objectClass.newInstance();
				Object entity = newObject.get(key);
				if(entity == null)
					return null;
				newObject.init(entity, attachment);
				put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
				log.info("get object not in cache, create it. key:{}, class:{}, instance:{}, fromDB:{}.", 
						key, objectClass, newObject, loadFromDB);
				return newObject;
			} catch (Exception e) {
				log.error("instantiate object failure, key:{}, class:{}, fromDB:{}, error:{}.", key, objectClass, loadFromDB, e);
				return null;
			}
		}finally{
			cacheLock.unlock();
		}
	}
	
	private SoftReference<CacheObject> putIfAbsent(String key, SoftReference<CacheObject> value) {
		if(putCount.incrementAndGet() % removeQueuedSize == 0)
			removeStaleEntries();
		return this.softCache.putIfAbsent(key, value);
	}
	
	private SoftReference<CacheObject> put(String key, SoftReference<CacheObject> value) {
		if(putCount.incrementAndGet() % removeQueuedSize == 0)
			removeStaleEntries();
		return this.softCache.put(key, value);
	}
	
	private static class CacheObjectReference<T extends CacheObject> extends SoftReference<T> {
		public String key;
		public CacheObjectReference(String key, T referent, ReferenceQueue<? super T> q) {
			super(referent, q);
			this.key = key;
		}
	}
	
	private void removeStaleEntries() {
    	try {
			cacheLock.lock();
	        for (Reference<? extends CacheObject> x; (x = refQueue.poll()) != null; ) {
	        	String key = ((CacheObjectReference<? extends CacheObject>)x).key;
	        	SoftReference<CacheObject> ref = softCache.get(key);
	        	if(ref != null && ref.get() == null)
	        		softCache.remove(key);
	        }
    	}finally{
    		cacheLock.unlock();
        }
    }
	
	public <T extends CacheObject<K, E>, K, E> Map<K, T> gets(Collection<K> keys, Class<T> objectClass, boolean loadFromDB, Object attachment) {
		if(keys == null || keys.isEmpty()) {
			log.info("gets object invalid keys:{}, class:{}, loadFromDB:{}.", 
				keys, objectClass, loadFromDB);
			return null;
		}
		int size = keys.size();
		Map<Object, CacheObject> inCacheObjects = new HashMap<Object, CacheObject>(size);
		List<K> notInCache = new ArrayList<K>(size);
		for(K key : keys) {
			if(key == null || key.toString().equals("0"))
				continue;
			T target = get(key, objectClass, false, attachment);
			if(target == null)
				notInCache.add(key);
			else
				inCacheObjects.put(key, target);
		}
		log.info("gets object, keys:{}, class:{}, loadFromDB:{}, notInCache:{}.", keys, objectClass, loadFromDB, notInCache); 
		if(loadFromDB && !notInCache.isEmpty()) {
			try {
				cacheLock.lock();
				Map<K, CacheObject<K, E>> gets = objectClass.newInstance().gets(notInCache);
				if(gets != null && !gets.isEmpty()) {
					log.info("gets object load from db:{}.", gets.keySet());
					for (Entry<K, ?> entrys : gets.entrySet()) {
						CacheObject value = (CacheObject)entrys.getValue();
						if(value != null) {
							Object key = entrys.getKey();
							String k = k(key, objectClass);
							SoftReference<CacheObject> ref = new SoftReference<CacheObject>(value);
							SoftReference<CacheObject> old = putIfAbsent(k, ref);
							if(old == null) {
								inCacheObjects.put(key, value);
							} else if(old.get() == null) {
								put(k, ref);
								inCacheObjects.put(key, value);
							} else {
								inCacheObjects.put(key, old.get());
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("gets object failure, keys:{}, class:{}, fromDB:{}, error:{}.", keys, objectClass, loadFromDB, e);
			} finally {
				cacheLock.unlock();
			}
		}
		Map<K, T> result = new LinkedHashMap<K, T>(size);
		for(K key : keys) {
			T t = (T) inCacheObjects.get(key);
			if(t != null)
				result.put(key, t);
		}
		log.info("gets object, keys:{}, class:{}, fromDB:{}, result:{}.", keys, objectClass, loadFromDB, result.keySet()); 
		return result;
	}
	
	public <T extends CacheObject, K> T create(K key, Class<T> objectClass, Object attachment) {
		if(key == null || key.toString().equals("0")) {
			log.info("create object invalid key:{}, class:{}.", key, objectClass);
			return null;
		}
		String k = k(key, objectClass);
		try {
			cacheLock.lock();
			SoftReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null) {
				T result = (T) ref.get();
				log.info("create object from cache, key:{}, class:{}, instance:{}.", 
						key, objectClass, result);				
				return result;
			}
			try {
				T newObject = (T) objectClass.newInstance();
				Object entity = newObject.create(attachment);
				newObject.init(entity, attachment);
				put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
				log.info("create object not in cache, create it. key:{}, class:{}, instance:{}.", 
						key, objectClass, newObject);
				return newObject;
			} catch (Exception e) {
				log.error("instantiate object failure, key:{}, class:{}, error:{}.", key, objectClass, e);
				return null;
			}
		}finally{
			cacheLock.unlock();
		}
	}
	
	public <T extends CacheObject> T put(T newObject) {
		if(newObject.getKey() == null || newObject.getKey().equals("0")) {
			log.info("put object invalid key:{}, class:{}, instance:{}.", 
				newObject.getKey(), newObject.getClass(), newObject);
			return null;
		}
		String k = k(newObject.getKey(), newObject.getClass());
		try {
			cacheLock.lock();
			SoftReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null)
				return (T) ref.get();
			
			put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
			log.info("put object, key:{}, class:{}, instance:{}.", newObject.getKey(), newObject.getClass(), newObject); 
			return newObject;
		}finally{
			cacheLock.unlock();
		}
	}
	
	public <T extends CacheObject> void remove(T object, boolean deleteEntity) {
		remove(object.getKey(), object.getClass(), deleteEntity);
	}
	
	public <T extends CacheObject> void remove(Object key, Class<T> objectClass, boolean deleteDBEntity) {
		if(key == null || key.toString().equals("0")) {
			log.info("remove object invalid key:{}, class:{}, deleteDBEntity:{}.", 
				key, objectClass, deleteDBEntity);			
			return;
		}
		String k = k(key, objectClass);
		try {
			cacheLock.lock();
			SoftReference<CacheObject> ref = this.softCache.remove(k);
			if(deleteDBEntity && ref != null && ref.get() != null) {
				try {
					ref.get().delete();
				} catch (Exception e) {
					log.error("remove object delete failure, key:{}, class:{}, deleteDBEntity:{}, error:{}.", 
						key, objectClass, deleteDBEntity, e); 
				}
			}
		}finally{
			cacheLock.unlock();
		}
	}

	public void saveAsync(CacheObject object) {
		this.saveQueue.saveAsync(object);
	}

	public void saveNow(CacheObject object) {
		this.saveQueue.saveNow(object);
	}
	
	public void saveAllNow() {
		this.saveQueue.saveAllNow();
	}
	
	private String k(Object key, Class<?> objectClass) {
		return objectClass.getSimpleName() + "_" + key.toString();
	}
}
