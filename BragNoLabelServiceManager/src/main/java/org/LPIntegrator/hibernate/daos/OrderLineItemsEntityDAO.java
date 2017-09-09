package org.LPIntegrator.hibernate.daos;

import java.util.List;

import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import io.dropwizard.hibernate.AbstractDAO;

public class OrderLineItemsEntityDAO extends AbstractDAO<OrderLineItemEntity> {

	public OrderLineItemsEntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
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
