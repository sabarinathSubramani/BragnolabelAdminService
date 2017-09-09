package org.LPIntegrator.hibernate.daos;

import java.util.List;
import java.util.Optional;
import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import io.dropwizard.hibernate.AbstractDAO;

public class OrderEntityDAO extends AbstractDAO<OrderEntity> {

	public OrderEntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<OrderEntity> findOrderByOrderId(long orderId){

		Query<OrderEntity> query = currentSession().createQuery("from OrderEntity where orderid=:id", OrderEntity.class);
		query.setParameter("id", orderId);
		return query.uniqueResultOptional();
	}

	public void insertOrderDetails(List <OrderEntity> orders){
		Session session = currentSession();

		Transaction beginTransaction = session.getTransaction();
		int i =1;
		for(OrderEntity entity : orders){
			session.saveOrUpdate(entity);
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
		beginTransaction.commit();
		session.close();
	}

	public List<OrderEntity> findOrderByOrderId(List<Long> orderIds){

		Query<OrderEntity> query = currentSession().createQuery("from OrderEntity where orderid IN (:orderids)", OrderEntity.class);
		query.setParameterList("orderIds", orderIds);
		return query.getResultList();
	}
	
	public int updateTrackingNumber(long orderId, long trackingId ){
		
		Query<OrderEntity> query = currentSession().createQuery("UPDATE OrderEntity set trackingNumber = :trackingId "  + 
	             "WHERE orderid = :orderid", OrderEntity.class);
		query.setParameter("trackingId", trackingId);
		query.setParameter("orderid", orderId);
		return query.executeUpdate();
	}
	
}
