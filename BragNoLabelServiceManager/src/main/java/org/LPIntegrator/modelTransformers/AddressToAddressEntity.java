package org.LPIntegrator.modelTransformers;

import java.util.function.Function;

import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.LPIntegrator.models.Address;

public class AddressToAddressEntity implements Function<Address, ShippingAddressEntity> {

	private long orderId;
	public AddressToAddressEntity(long orderId){
		this.orderId = orderId;
	}
	@Override
	public ShippingAddressEntity apply(Address t) {
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
	}

}
