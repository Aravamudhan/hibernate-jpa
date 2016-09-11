package com.amudhan.jpatest.model.collections.embeddablesetofembeddables;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractUserTest;

public class EmbeddedWithEmbeddableSetTest extends AbstractUserTest {

	@Test
	@Transactional
	@Commit
	public void userWithEmbeddablesInsert(){
		User user = new User();
		user.setUserName("RandomUserName");
		Address address = new Address("Astreet", "123", "ACity");
		address.getContacts().add(new Contact("ContactAA"));
		address.getContacts().add(new Contact("ContactAB"));
		address.getContacts().add(new Contact("ContactAC"));
		user.setAddress(address);
		entityManager.persist(user);
	}
}
