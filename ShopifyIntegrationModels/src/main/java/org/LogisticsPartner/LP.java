package org.LogisticsPartner;

public enum LP {
	DELHIVERY(1),
	DELHIVERY_WH(0);
	
	private int id;
	private LP(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	public static LP getLPbyId(int id) {
		for(LP lp : LP.values()) {
			if(lp.getId() == id)
				return lp;
		}
		return null;
	}
}
