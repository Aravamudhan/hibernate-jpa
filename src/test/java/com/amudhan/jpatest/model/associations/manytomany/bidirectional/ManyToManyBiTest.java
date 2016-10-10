package com.amudhan.jpatest.model.associations.manytomany.bidirectional;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ManyToManyBiTest extends AbstractItemTest {

	private long anotherPersistedId;
	
	@Test(priority = 1)
	@Transactional
	@Commit
	public void manyToManyBiInsert(){
		Category categoryOne = new Category("FirstCategory"); 
		Category categoryTwo = new Category("SecondCategory");
		Category categoryThree = new Category("ThirdCategory");
		Item itemOne = new Item("FirstItem");
		Item itemSecond = new Item("SecondItem");
		Item itemThird = new Item("ThirdItem");
		
		categoryOne.getItems().add(itemOne);
		categoryOne.getItems().add(itemSecond);
		categoryOne.getItems().add(itemThird);
		
		categoryTwo.getItems().add(itemThird);
		categoryThree.getItems().add(itemThird);
		
		entityManager.persist(categoryOne);
		entityManager.persist(categoryTwo);
		entityManager.persist(categoryThree);
		
		persistedId = categoryOne.getId();
		anotherPersistedId = categoryTwo.getId();
		
	}
	
	@Test(priority = 2)
	@Transactional
	@Commit
	public void manyToManyBiDisplay(){
		/* If an entity is returned by the result of find method, that entity
		 * becomes managed and part of that persistence context. No need to
		 * call EntityManager#persist method.*/
		Category category = entityManager.find(Category.class, persistedId);
		logger.info(category.toString());
		for(Item item : category.getItems()){
			logger.info(item.toString());
		}
		Category categoryAnother = entityManager.find(Category.class, anotherPersistedId);
		logger.info(categoryAnother.toString());
		for(Item item : categoryAnother.getItems()){
			logger.info(item.toString());
		}
		/* These two changes will automatically be updated.*/
		category.setCategoryName("Great category");
		categoryAnother.setCategoryName("Another great category");
	}
}
