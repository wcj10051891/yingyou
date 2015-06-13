package xgame.core.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
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
	private ConcurrentHashMap<String, WeakReference<CacheObject>> softCache = new ConcurrentHashMap<String, WeakReference<CacheObject>>();
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
	
	/**
	 * 从缓存中获取缓存对象，如果缓存中没有，且loadFromDB为true，会从数据库中加载，调用缓存对象的get方法，查出entity对象并调用init方法
	 * @param key			缓存对象的key
	 * @param objectClass	缓存对象类
	 * @param loadFromDB	是否从数据库加载
	 * @param contextParam	初始化缓存对象所需的辅助参数，传给init方法
	 * @return
	 */
	public <T extends CacheObject, K> T get(K key, Class<T> objectClass, boolean loadFromDB, Object... contextParam) {
		if(key == null || key.toString().equals("0")) {
			log.info("get object invalid key:{}, class:{}, loadFromDB:{}", 
				key, objectClass, loadFromDB);
			return null;
		}
		String k = k(key, objectClass);
		try {
			cacheLock.lock();
			WeakReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null) {
				T result = (T) ref.get();
				log.info("get object from cache, key:{}, class:{}, instance:{}, fromDB:{}", 
						key, objectClass, result, loadFromDB);				
				return result;
			}
			
			if(!loadFromDB) {
				log.info("get object not in cache, nonCreate it. key:{}, class:{}, fromDB:{}", 
						key, objectClass, loadFromDB);	
				return null;
			}
			
			try {
				T newObject = (T) objectClass.newInstance();
				Object entity = newObject.get(key);
				if(entity == null)
					return null;
				newObject.init(entity, contextParam);
				put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
				log.info("get object not in cache, load it. key:{}, class:{}, instance:{}, fromDB:{}", 
						key, objectClass, newObject, loadFromDB);
				return newObject;
			} catch (Exception e) {
				log.error("instantiate object failure, key:{}, class:{}, fromDB:{}, error:{}", key, objectClass, loadFromDB, e);
				return null;
			}
		} finally {
			cacheLock.unlock();
		}
	}
	
	private WeakReference<CacheObject> putIfAbsent(String key, WeakReference<CacheObject> value) {
		if(putCount.incrementAndGet() % removeQueuedSize == 0)
			removeStaleEntries();
		return this.softCache.putIfAbsent(key, value);
	}
	
	private WeakReference<CacheObject> put(String key, WeakReference<CacheObject> value) {
		if(putCount.incrementAndGet() % removeQueuedSize == 0)
			removeStaleEntries();
		return this.softCache.put(key, value);
	}
	
	private static class CacheObjectReference<T extends CacheObject> extends WeakReference<T> {
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
	        	WeakReference<CacheObject> ref = softCache.get(key);
	        	if(ref != null && ref.get() == null)
	        		softCache.remove(key);
	        }
    	}finally{
    		cacheLock.unlock();
        }
    }
	
	/**
	 * 批量获取缓存对象，如果缓存中没有，且loadFromDB为true，会从数据库中加载，调用缓存对象的get方法，查出entity对象并调用init方法
	 * @param keys				key数组
	 * @param objectClass		缓存类
	 * @param loadFromDB		是否从数据库加载
	 * @param contextParam		初始化缓存对象所需的辅助参数，传给init方法
	 * @return
	 */
	public <T extends CacheObject<K, E>, K, E> Map<K, T> gets(Collection<K> keys, Class<T> objectClass, boolean loadFromDB, Object... contextParam) {
		if(keys == null || keys.isEmpty()) {
			log.info("gets object invalid keys:{}, class:{}, loadFromDB:{}", 
				keys, objectClass, loadFromDB);
			return null;
		}
		int size = keys.size();
		Map<Object, CacheObject> inCacheObjects = new HashMap<Object, CacheObject>(size);
		List<K> notInCache = new ArrayList<K>(size);
		for(K key : keys) {
			if(key == null || key.toString().equals("0"))
				continue;
			T target = get(key, objectClass, false, contextParam);
			if(target == null)
				notInCache.add(key);
			else
				inCacheObjects.put(key, target);
		}
		log.info("gets object, keys:{}, class:{}, loadFromDB:{}, notInCache:{}", keys, objectClass, loadFromDB, notInCache); 
		if(loadFromDB && !notInCache.isEmpty()) {
			try {
				cacheLock.lock();
				Map<K, E> entitys = objectClass.newInstance().gets(notInCache);
				if(entitys != null && !entitys.isEmpty()) {
					log.info("gets object load from db:{}", entitys);
					for (Entry<K, E> entry : entitys.entrySet()) {
						Object key = entry.getKey();
						String k = k(key, objectClass);
						CacheObject<K, E> o = objectClass.newInstance();
						o.init(entry.getValue(), contextParam);
						WeakReference<CacheObject> ref = new WeakReference<CacheObject>(o);
						WeakReference<CacheObject> old = putIfAbsent(k, ref);
						if(old == null) {
							inCacheObjects.put(key, o);
						} else if(old.get() == null) {
							put(k, ref);
							inCacheObjects.put(key, o);
						} else {
							inCacheObjects.put(key, old.get());
						}
					}
				}
			} catch (Exception e) {
				log.error("gets object failure, keys:{}, class:{}, fromDB:{}, error:{}", keys, objectClass, loadFromDB, e);
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
		log.info("gets object, keys:{}, class:{}, fromDB:{}, result:{}", keys, objectClass, loadFromDB, result.keySet()); 
		return result;
	}
	
	/**
	 * 新建缓存对象，即插入相应记录到数据库，并调用init方法初始化
	 * @param objectClass		缓存类
	 * @param contextParam		初始化缓存对象所需的辅助参数，传给init方法
	 * @return
	 */
	public <T extends CacheObject, K> T create(Class<T> objectClass, Object... contextParam) {
		try {
			cacheLock.lock();
			T newObject = (T) objectClass.newInstance();
			Object entity = newObject.create(contextParam);
			newObject.init(entity, contextParam);
			Object key = newObject.getKey();
			String k = k(key, objectClass);

			WeakReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null) {
				T result = (T) ref.get();
				log.info("create object already in cache, key:{}, class:{}, instance:{}", 
						key, objectClass, result);				
				return result;
			}
			
			put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
			log.info("create object put to cache, key:{}, class:{}, instance:{}", 
					key, objectClass, newObject);
			return newObject;
		} catch (Exception e) {
			log.error("instantiate object failure, class:{}, error:{}", objectClass, e);
			return null;
		} finally {
			cacheLock.unlock();
		}
	}
	

	/**
	 * 用数据库实体来初始化出缓存对象并放入缓存
	 * @param newObject
	 * @return
	 */
	public <T extends CacheObject, E> T init(Class<T> objectClass, Object key, E entity, Object... contextParam) {
		if(key == null || key.equals("0")) {
			log.info("put object invalid key:{}, class:{}", key, objectClass);
			return null;
		}
		String k = k(key, objectClass);
		try {
			cacheLock.lock();
			WeakReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null)
				return (T) ref.get();
			
			T newObject = (T) objectClass.newInstance();
			newObject.init(entity, contextParam);
			
			put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
			log.info("put object, key:{}, class:{}, instance:{}", newObject.getKey(), newObject.getClass(), newObject); 
			return newObject;
		}  catch (Exception e) {
			log.error("instantiate object failure, class:{}, error:{}", objectClass, e);
			return null;
		} finally {
			cacheLock.unlock();
		}
	}
	
	/**
	 * 把缓存对象放入缓存中
	 * @param newObject
	 * @return
	 */
	public <T extends CacheObject> T put(T newObject) {
		if(newObject.getKey() == null || newObject.getKey().equals("0")) {
			log.info("put object invalid key:{}, class:{}, instance:{}", 
				newObject.getKey(), newObject.getClass(), newObject);
			return null;
		}
		String k = k(newObject.getKey(), newObject.getClass());
		try {
			cacheLock.lock();
			WeakReference<CacheObject> ref = softCache.get(k);
			if(ref != null && ref.get() != null)
				return (T) ref.get();
			
			put(k, new CacheObjectReference<CacheObject>(k, newObject, refQueue));
			log.info("put object, key:{}, class:{}, instance:{}", newObject.getKey(), newObject.getClass(), newObject); 
			return newObject;
		}finally{
			cacheLock.unlock();
		}
	}
	
	/**
	 * 把缓存对象从缓存中移除，如果deleteEntity为true会调用缓存对象的delete方法删除对应的数据库记录
	 * @param object		缓存对象
	 * @param deleteEntity	是否删除数据库记录
	 */
	public <T extends CacheObject> void remove(T object, boolean deleteEntity) {
		remove(object.getKey(), object.getClass(), deleteEntity);
	}
	
	/**
	 * 把缓存对象从缓存中移除，如果deleteEntity为true会调用缓存对象的delete方法删除对应的数据库记录
	 * @param key				缓存对象的key
	 * @param objectClass		缓存类
	 * @param deleteEntity		是否调用缓存对象的delete方法删除数据库记录
	 */
	public <T extends CacheObject> void remove(Object key, Class<T> objectClass, boolean deleteEntity) {
		if(key == null || key.toString().equals("0")) {
			log.info("remove object invalid key:{}, class:{}, deleteEntity:{}", 
				key, objectClass, deleteEntity);			
			return;
		}
		String k = k(key, objectClass);
		try {
			cacheLock.lock();
			WeakReference<CacheObject> ref = this.softCache.remove(k);
			if(deleteEntity && ref != null && ref.get() != null) {
				try {
					ref.get().delete();
				} catch (Exception e) {
					log.error("remove object delete failure, key:{}, class:{}, deleteDBEntity:{}, error:{}", 
						key, objectClass, deleteEntity, e); 
				}
			}
		}finally{
			cacheLock.unlock();
		}
	}

	/**
	 * 异步保存，根据缓存对象的{@link SaveDelay}延迟秒数来调度保存
	 * @param object
	 */
	public void saveAsync(CacheObject object) {
		this.saveQueue.saveAsync(object);
	}

	/**
	 * 立即保存
	 * @param object
	 */
	public void saveNow(CacheObject object) {
		this.saveQueue.saveNow(object);
	}
	
	/**
	 * 立即保存所有已定时调度还未保存的对象
	 */
	public void saveAllNow() {
		this.saveQueue.saveAllNow();
	}
	
	private String k(Object key, Class<?> objectClass) {
		return objectClass.getSimpleName() + "_" + key.toString();
	}
}
