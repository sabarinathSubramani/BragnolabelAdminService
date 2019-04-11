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

public class OrderLineItemsEntityDAO extends AbstractDAO<OrderLineItemEntity> {

	static Logger logger = Logger.getLogger(OrderLineItemsEntityDAO.class);
	@Inject
	public OrderLineItemsEntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public void updateTrackingNumber(Map<Long, String> trackingNumberMap){
		if(trackingNumberMap!=null && !trackingNumberMap.isEmpty()){
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
		}else{
			logger.error("tracking number map is either empty or null. skipping update");
		}
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
	
	public Stream<OrderLineItemEntity> orderEntity(Collection<Long> orderLineItemIds){
		List<OrderLineItemEntity> list = Collections.EMPTY_LIST;
		if(orderLineItemIds.size()>0){
			Session session = currentSession();
			String hql = "from OrderLineItemEntity where orderItemId IN (:orderLineItemids)";
			Query<OrderLineItemEntity> createQuery = session.createQuery(hql, OrderLineItemEntity.class);
			createQuery.setParameterList("orderLineItemids", orderLineItemIds);
			list =createQuery.getResultList();
		}
		return list.stream();
	}
	
	public Stream<OrderLineItemEntity> getInTransitOrdersByLP(LP lp){
		List<OrderLineItemEntity> list = Lists.newArrayList();
		if(lp != null){
			Session session = currentSession();
			String hql = "from OrderLineItemEntity as ole join ole.trackingNumber st where st.shipment_status !=:status and st.lp_id =:lpId";
			Query<OrderLineItemEntity> createQuery = session.createQuery(hql, OrderLineItemEntity.class);
			createQuery.setParameter("lpId", lp.getId());
			createQuery.setParameter("status", ShipmentStatus.DELIVERED);
			list =createQuery.getResultList();
		}
		return list.stream();
	}
	
	public Stream<OrderLineItemEntity> getOrdersByTrackingNumbers(Set<String> trackingNumbers){
		
		List<ShipmentTrackingEntity> sheList = new ArrayList();
		trackingNumbers.stream().map(t -> {ShipmentTrackingEntity she = new ShipmentTrackingEntity();
		she.setTrackingNumber(t);
		return she;}).forEach(sheList::add);
		List<OrderLineItemEntity> list = Collections.EMPTY_LIST;
		if(trackingNumbers.size() > 0){
			Session session = currentSession();
			String hql = "FROM OrderLineItemEntity ole where ole.trackingNumber IN :trackingNumbers";
			Query<OrderLineItemEntity> createQuery = session.createQuery(hql, OrderLineItemEntity.class);
			createQuery.setParameterList("trackingNumbers", sheList);
			list =createQuery.list();
		}
		return list.stream();
	}

	public void saveOrderLineItemsEntity(OrderLineItemEntity[] orderLineEntity) {
		Session session = currentSession();
		for(OrderLineItemEntity oel : orderLineEntity ) {
			session.saveOrUpdate(oel);
		}
		session.flush();
	}
}
