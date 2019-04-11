package org.LogisticsPartner.Client;

import javax.ws.rs.client.Client;

import org.Delhivery.Client.DelhiveryLMClientImpl;
import org.Delhivery.Client.DelhiveryWHClientImpl;
import org.LogisticsPartner.LP;

public class LPClientFactory {

	private static DelhiveryWHClientImpl delhiveryClientImpl =  null;
	private static DelhiveryLMClientImpl delhiveryLMClientImpl =  null;
	
	public synchronized static LPClient getClient(LP lp, Client client){

		switch(lp){

		case DELHIVERY_WH:{
			if(delhiveryClientImpl == null)
				delhiveryClientImpl = new DelhiveryWHClientImpl(client);
			return delhiveryClientImpl;
		}
		case DELHIVERY:{
			if(delhiveryLMClientImpl == null)
				delhiveryLMClientImpl =  new DelhiveryLMClientImpl(client);
			return delhiveryLMClientImpl;
		}
		default:
			return null;
		}
	}

}
