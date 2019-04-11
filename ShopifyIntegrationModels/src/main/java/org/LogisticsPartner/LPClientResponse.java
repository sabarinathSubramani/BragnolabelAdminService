package org.LogisticsPartner;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class LPClientResponse {

	private Error error;
	private LPClientRequest request;
	
	public void initError(){
		if(this.getError() ==  null) {
			this.setError( new Error());
		}
		if(this.getError().getErrorMessages() == null) {
			this.getError().setErrorMessages(Lists.newArrayList());
		}
	}
	
}
