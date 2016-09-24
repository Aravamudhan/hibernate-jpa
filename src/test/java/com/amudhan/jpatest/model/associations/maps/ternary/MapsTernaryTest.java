package com.amudhan.jpatest.model.associations.maps.ternary;

import java.util.Map.Entry;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class MapsTernaryTest extends AbstractItemTest {

	@Test(priority =1)
	@Transactional
	@Commit
	public void mapsTernaryInsert(){
		Category categoryOne = new Category("CategoryOne");
		Item itemOne = new Item("ItemOne");
		User userOne = new User("UserOne");
		categoryOne.getItemAddedBy().put(itemOne, userOne);
		entityManager.persist(userOne);
		entityManager.persist(itemOne);
		entityManager.persist(categoryOne);
		persistedId = categoryOne.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void mapsTernaryDisplay(){
		Category categoryOne = entityManager.find(Category.class, persistedId);
		logger.info(categoryOne.toString());
		for(Entry<Item, User> itemUser : categoryOne.getItemAddedBy().entrySet()){
			logger.info(itemUser.getKey().toString());
			logger.info(itemUser.getValue().toString());
		}
	}
}
