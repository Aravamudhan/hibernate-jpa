package com.amudhan.jpatest.model.associations.onetomany.embeddable.jointable;

import java.time.LocalDateTime;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToManyEmbeddableJoinTableTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToManyEmbeddableUserWithJoinTableInsert(){
		User user = new User();
		user.setName("Awesome user");
		Address shippingAddress = new Address("AS street","612001", "Paris");
		user.setShippingAddress(shippingAddress);
		entityManager.persist(user);
		Shipment shipmentOne = new Shipment(LocalDateTime.now());
		shippingAddress.getDeliveries().add(shipmentOne);
		entityManager.persist(shipmentOne);
		persistedId = user.getId();
	}

	@Test(priority = 2)
	@Transactional
	public void oneToManyEmbeddableUserWithJoinTableDisplay(){
		User user = entityManager.find(User.class, persistedId);
		logger.info(user.toString());
		logger.info(user.getShippingAddress().toString());
		for(Shipment shipment : user.getShippingAddress().getDeliveries()){
			logger.info(shipment.toString());
		}		
	}
	
}
