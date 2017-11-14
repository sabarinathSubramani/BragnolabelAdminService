package org.LPIntegrator.hibernate.daos;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.TemporalType;

import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.inject.Inject;

import io.dropwizard.hibernate.AbstractDAO;

public class OrderEntityDAO extends AbstractDAO<OrderEntity> {

	static Logger logger = Logger.getLogger(OrderEntityDAO.class);
	@Inject
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

	public void updateTrackingNumber(long orderId, long trackingId ){

		Query<OrderEntity> query = currentSession().createQuery("UPDATE OrderEntity set trackingNumber = :trackingId "  + 
				"WHERE orderid = :orderid");
		query.setParameter("trackingId", trackingId);
		query.setParameter("orderid", orderId);
		query.executeUpdate();
		currentSession().getTransaction().commit();
	}

	public List<OrderEntity> getOrdersForWareHouse(){

		List<OrderEntity> ordersList = Collections.EMPTY_LIST;
		try{
			Query<OrderEntity> query = currentSession().createQuery("FROM OrderEntity where pushedToWareHouse = 0 and orderStatus NOT LIKE '%cancelled%' and createdAt <= :dateTimeafterCoolingPeriod");
			Date newDate = DateUtils.addHours(Calendar.getInstance().getTime(), -6);
			query.setParameter("dateTimeafterCoolingPeriod", newDate, TemporalType.DATE);
			ordersList = query.list();
		}catch(Exception e){
			logger.error("error while retrieving orders", e);
		}
		return ordersList;

	}

	public void updateOrderStatus(long orderId, String status ){

		Query query = currentSession().createQuery("UPDATE OrderEntity set orderStatus = :status "  + 
				"WHERE orderid = :orderid");
		query.setParameter("status", status);
		query.setParameter("orderid", orderId);
		query.executeUpdate();
		currentSession().getTransaction().commit();
	}

	public void updatePushedToWareHouse(long orderId){

		Query query = currentSession().createQuery("UPDATE OrderEntity set pushedToWareHouse = 1 "  + 
				"WHERE orderid = :orderid");
		query.setParameter("orderid", orderId);
		query.executeUpdate();
		currentSession().getTransaction().commit();
	}



}
