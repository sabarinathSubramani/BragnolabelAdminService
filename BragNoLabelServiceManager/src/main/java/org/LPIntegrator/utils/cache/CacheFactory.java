package org.LPIntegrator.utils.cache;

import org.apache.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheFactory {

	static Logger logger = Logger.getLogger(CacheFactory.class);

	public static <K, V> LoadingCache<K,V> newCacheObject(Cacheable<K, V> cache){
		try{

			CacheBuilder<Object, Object> newBuilder = CacheBuilder.newBuilder();

			if(cache.sizeBaseExpiration()!= null& cache.sizeBaseExpiration().getMaxSize()>0)
				newBuilder.maximumSize(cache.sizeBaseExpiration().getMaxSize());
			if(cache.timedExpiration()!=null){
				if(cache.timedExpiration().isExpiresAfterWrite())
					newBuilder.expireAfterWrite(cache.timedExpiration().getExpiryTime(), cache.timedExpiration().getExpiryTimeUnit());
				else
					newBuilder.expireAfterAccess(cache.timedExpiration().getExpiryTime(), cache.timedExpiration().getExpiryTimeUnit());
			}
			if(cache.refreshPolicy()!=null && cache.refreshPolicy().isAllowRefresh()){
				newBuilder.refreshAfterWrite(cache.refreshPolicy().getRefreshWaitTime(), cache.refreshPolicy().getTimeUnit());
			}

			if(cache.initialCapacity()>0)
				newBuilder.initialCapacity(cache.initialCapacity());

			if(cache.concurrencyLevel()>0)
				newBuilder.initialCapacity(cache.concurrencyLevel());

			return newBuilder.build(new CacheLoader<K, V>(){

				@Override
				public V load(K key) throws Exception {
					return cache.loadValue(key);
				}

			});
		}catch(Exception e){
			logger.error("Unable initialize cache - "+cache.getClass().getName(), e);
			return null;
		}
	}

}
