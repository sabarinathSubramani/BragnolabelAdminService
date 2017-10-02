package org.LPIntegrator.utils.cache;

import java.util.function.Function;

import lombok.Data;

@Data
public class SizeBasedExpiration<K, V> {

	private long maxSize;
	private Function<K, V> weighers;
	
	public SizeBasedExpiration(long maxSize){
		this.maxSize=maxSize;
	}
}
