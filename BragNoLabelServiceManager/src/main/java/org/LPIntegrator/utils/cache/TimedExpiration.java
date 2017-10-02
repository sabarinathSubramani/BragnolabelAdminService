package org.LPIntegrator.utils.cache;

import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class TimedExpiration {

	private boolean expiresAfterWrite;
	private long expiryTime;
	private TimeUnit expiryTimeUnit;
	
	public TimedExpiration(boolean expiresAfterWrite, long expiryTime, TimeUnit expiryTimeUnit){
		this.expiresAfterWrite=expiresAfterWrite;
		this.expiryTime= expiryTime;
		this.expiryTimeUnit= expiryTimeUnit;
	}
}
