package xgame.core.cache;

import java.util.List;
import java.util.Map;

/**
 * 缓存对象使用方式：
 * 	CacheObject cacheObject = CacheService.get(id,class,loadFromDB)
 *   先在内存缓存里查询对象是否已经存在，若不存在且loadFromDB为true，会调用class对应的对象的get(key)去数据库查询，返回对应的db entity对象，
 *	1.若db entity存在，则由CacheService调用缓存对象的init(entity)方法来进行对象初始化，随后将此对象加入内存缓存，
 *   	对它的一些修改可以调用CacheService的saveAsync(异步保存)或saveNow(同步立即保存)来统一保存
 *	2.若db entity不存在，数据库里没有数据，则由业务需求来决定创建时机和方式。
 *		1)在缓存对象的get方法里写逻辑来insert数据库entity(entity的key设置),然后返回entity给CacheService.get,他会调用init方法初始化缓存对象，并成功加入缓存。
 *		2)缓存对象的get方法直接返回null，CacheService.get返回给调用代码对象不存在，由外部决定何时创建。
 *		3)业务代码需要创建一个对象的时候，可以实例化CacheObject和对应的db entity(entity的key设置)，然后调用CacheObject的init方法传入entity进行初始化，
 *			调用CacheObject的insert方法插入数据库数据落地，然后调用CacheService的put方法将CacheObject放入缓存。
 */
public interface CacheObject<K, E> {
	CacheObject<K, E> init(E entity);
	E get(K key);
	Map<K, CacheObject<K, E>> gets(List<K> keys);
	void insert();
	void delete();
	void update();
	K getKey();
}
