package com.amudhan.jpatest.model.associations.onetomany.jointable;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToManyJoinTableTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToManyJoinTableItemInsert(){
		Item item = new Item();
		item.setItemName("Awesome item 1");
		User user = new User();
		user.setName("The User");
		entityManager.persist(user);
		entityManager.persist(item);
		user.getBoughtItems().add(item);
		item.setBuyer(user);
		persistedId = user.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToManyJoinTableDisplay(){
		User user = entityManager.find(User.class, persistedId);
		logger.info(user.toString());
		for(Item item : user.getBoughtItems()){
			logger.info(item.toString());
		}
	}
}
