package org.LogisticsPartner.Client;

import javax.ws.rs.client.Client;

import org.LogisticsPartner.LP;

public class LPClientFactory {

	private static DelhiveryClientImpl delhiveryClientImpl =  null;
	
	public synchronized static LPClient getClient(LP lp, Client client){

		switch(lp){

		case DELHIVERY:{
			if(delhiveryClientImpl == null)
				return new DelhiveryClientImpl(client);
		}
		default:
			return null;
		}
	}

}
