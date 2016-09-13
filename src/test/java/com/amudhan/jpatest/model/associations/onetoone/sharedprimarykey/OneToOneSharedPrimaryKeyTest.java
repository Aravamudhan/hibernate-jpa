package com.amudhan.jpatest.model.associations.onetoone.sharedprimarykey;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToOneSharedPrimaryKeyTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneTOneSharedPrimaryKeyPersist(){
		Address address = new Address("AB Street", "738373", "Paris");
		entityManager.persist(address);
		/* If User has auto generated id, or uses any other id other the id of the Address, referential integrity constraint violation exception is thrown.
		 * The reason is Address is marked with @PrimaryKeyJoinColumn*/
		User user = new User(address.getId(), "Awesome User");
		user.setShippingAddress(address);
		entityManager.persist(user);
		persistedId = user.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneTOneSharedPrimaryKeyPrint(){
		User user = entityManager.find(User.class, persistedId);
		logger.info("User "+user);
		logger.info("Address "+user.getShippingAddress());
	}
}
