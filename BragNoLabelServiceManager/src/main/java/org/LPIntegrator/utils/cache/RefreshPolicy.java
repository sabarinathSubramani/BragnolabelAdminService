package org.LPIntegrator.utils.cache;

import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class RefreshPolicy {

	public RefreshPolicy(boolean b, long i, TimeUnit timeUnit) {
		this.allowRefresh =b;
		this.refreshWaitTime=i;
		this.timeUnit=timeUnit;
	}

	private boolean allowRefresh;
	
	private long refreshWaitTime;
	
	private TimeUnit timeUnit;
	
}
