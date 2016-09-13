package com.amudhan.jpatest.model.associations.onetoone.foreignkeygenerator;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToOneForeignKeyGeneratorTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void addressWithOneToOneForeignKeyGeneratorInsert(){
		User user = new User("Awesome another user");
		Address address = new Address("AB Street", "738373", "Paris",user);
		user.setShippingAddress(address);
		entityManager.persist(user);
		persistedId = user.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void addressWithOneToOneForeignKeyGeneratorDisplay(){
		User user = entityManager.find(User.class, persistedId);
		logger.info(user.toString());
		logger.info(user.getShippingAddress().toString());
		
	}
}
