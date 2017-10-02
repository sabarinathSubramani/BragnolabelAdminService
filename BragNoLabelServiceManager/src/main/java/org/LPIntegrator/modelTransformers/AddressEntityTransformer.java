package org.LPIntegrator.modelTransformers;

import java.util.function.Function;

import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.ShopifyInegration.models.Address;

public class AddressEntityTransformer  {

	private long orderId;
	public AddressEntityTransformer(long orderId){
		this.orderId = orderId;
	}
	
	
	public Function<Address, ShippingAddressEntity> toAddressEntity(){
		return t ->{

			ShippingAddressEntity sae = new ShippingAddressEntity(orderId);
			sae.setAddress1(t.getAddressLine1());
			sae.setAddress2(t.getAddressLine2());
			sae.setCity(t.getCity());
			sae.setCountry(t.getCountry());
			sae.setFullName((t.getFirstName()!= null? t.getFirstName():"")+" "+(t.getLastName()!= null? t.getLastName():""));
			sae.setPincode(t.getZip());
			sae.setState(t.getState());
			sae.setPhone(t.getPhone());
			sae.setEmail(t.getEmail());
			return sae;
		};
	}
	
	public Function<ShippingAddressEntity, Address> toAddress(){
		return t ->{

			Address address = new Address();
			address.setFullName(t.getFullName());
			address.setAddressLine1(t.getAddress1());
			address.setAddressLine2(t.getAddress2());
			address.setCity(t.getCity());
			address.setCountry(t.getCountry());
			address.setEmail(t.getEmail());
			address.setPhone(t.getPhone());
			address.setState(t.getState());
			address.setZip(t.getPincode());
			return address;
		};
	}

}
