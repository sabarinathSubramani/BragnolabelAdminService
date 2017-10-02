package org.LPIntegrator.hibernate.daos;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.inject.Inject;

import io.dropwizard.hibernate.AbstractDAO;

public class OrderLineItemsEntityDAO extends AbstractDAO<OrderLineItemEntity> {

	@Inject
	public OrderLineItemsEntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public void updateTrackingNumber(Map<Long, String> trackingNumberMap){
		Session session = currentSession();
		Transaction beginTransaction = session.getTransaction();
		for(Entry<Long, String> entry : trackingNumberMap.entrySet()){
		String hql = "UPDATE OrderLineItemEntity set trackingNumber = :trackingNumber "  + 
	             "WHERE orderItemId = :orderItemId";
		Query<OrderLineItemEntity> createQuery = session.createQuery(hql);
		createQuery.setParameter("trackingNumber", entry.getValue());
		createQuery.setParameter("orderItemId", entry.getKey());
		createQuery.executeUpdate();
		}
		beginTransaction.commit();
		session.close();
	}

	public void insertOrderDetails(List <OrderLineItemEntity> orderLineItems){
		Session session = currentSession();

		Transaction beginTransaction = session.getTransaction();
		int i =1;
		for(OrderLineItemEntity entity : orderLineItems){
			super.persist(entity);
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
		beginTransaction.commit();
		session.close();
	}
}
