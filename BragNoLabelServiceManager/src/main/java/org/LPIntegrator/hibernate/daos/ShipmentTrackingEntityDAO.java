package org.LPIntegrator.hibernate.daos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShipmentTrackingEntity;
import org.LogisticsPartner.LP;
import org.LogisticsPartner.ShipmentStatus;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.inject.Inject;

import io.dropwizard.hibernate.AbstractDAO;
import jersey.repackaged.com.google.common.collect.Lists;

public class ShipmentTrackingEntityDAO extends AbstractDAO<OrderLineItemEntity> {

	static Logger logger = Logger.getLogger(ShipmentTrackingEntityDAO.class);
	@Inject
	public ShipmentTrackingEntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}


	public void saveShipmentTrackingEntity(ShipmentTrackingEntity shipmentTrackingEntity) {
		Session session = currentSession();
		session.saveOrUpdate(shipmentTrackingEntity);
		session.flush();
	}
}
