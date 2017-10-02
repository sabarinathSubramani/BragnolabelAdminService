package org.LPIntegrator.utils.cache;

import java.util.Map;

public interface Cacheable<K, V> {
	
	public Map<K, V> preLoad();

	public V loadValue(K key);
	
	public TimedExpiration timedExpiration();
	
	public SizeBasedExpiration<K, V> sizeBaseExpiration();
	
	public RefreshPolicy refreshPolicy();
	
	public int initialCapacity();
	
	public int concurrencyLevel();
	
}
