package com.amudhan.jpatest.model.associations.onetoone.foreignkey;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToOneForeignKeyTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToOneForeignKeyUserInsert(){
		User user = new User();
		user.setName("Awesome user name");
		Address shippingAddress = new Address("AB Street", "738373", "Paris");
		user.setShippingAddress(shippingAddress);
		entityManager.persist(user);
		persistedId = user.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToOneForeignKeyUserPrint(){
		User user = entityManager.find(User.class, persistedId);
		logger.info("User ID: "+user.getId()+" User name: "+user.getName());
		logger.info(user.getShippingAddress().toString());
	}
}
