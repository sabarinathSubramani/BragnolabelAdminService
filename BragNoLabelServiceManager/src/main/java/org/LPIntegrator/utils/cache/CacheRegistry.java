package org.LPIntegrator.utils.cache;

import java.util.concurrent.ConcurrentHashMap;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;

public class CacheRegistry {

	@SuppressWarnings("rawtypes")
	private static ConcurrentHashMap<CacheEnum, Cache> cacheMap = new ConcurrentHashMap<>();

	public synchronized static <K,V> void initializeCache(CacheEnum cacheName, Cacheable<K, V> cacheable){
		//if cache is already loaded, do not initialize it again.
		if(getCache(cacheName) == null){
			LoadingCache<K, V> cache = CacheFactory.newCacheObject(cacheable);
			cacheMap.put(cacheName, cache);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Cache getCache(CacheEnum cacheName){
		return cacheMap.get(cacheName);
	}
	
	@SuppressWarnings("rawtypes")
	public static LoadingCache getAsLoadingCache(CacheEnum cacheName){
		return (LoadingCache)getCache(cacheName);
	}
}
