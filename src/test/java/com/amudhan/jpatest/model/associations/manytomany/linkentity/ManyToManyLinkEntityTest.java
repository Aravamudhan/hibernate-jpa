package com.amudhan.jpatest.model.associations.manytomany.linkentity;

import java.time.LocalDateTime;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ManyToManyLinkEntityTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void manyToManyLinkEntityInsert(){
		Category categoryOne = new Category("FirstCategory"); 
		Item itemOne = new Item("FirstItem");
		Item itemSecond = new Item("SecondItem");
		Item itemThird = new Item("ThirdItem");
		
		entityManager.persist(categoryOne);
		entityManager.persist(itemOne);
		entityManager.persist(itemSecond);
		entityManager.persist(itemThird);

		LocalDateTime now = LocalDateTime.now();
		CategorizedItem categorizedItemOne = new CategorizedItem("UserOne", now, categoryOne, itemOne);
		CategorizedItem categorizedItemTwo = new CategorizedItem("UserOne", now, categoryOne, itemSecond);
		CategorizedItem categorizedItemThree = new CategorizedItem("UserOne", now, categoryOne, itemThird);
		
		entityManager.persist(categorizedItemOne);
		entityManager.persist(categorizedItemTwo);
		entityManager.persist(categorizedItemThree);
		
		persistedId = categoryOne.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void manyToManyLinkEntityDisplay(){
		Category category = entityManager.find(Category.class, persistedId);
		logger.info(category.toString());
		for(CategorizedItem categorizedItem : category.getCategorizedItems()){
			logger.info(categorizedItem.toString());
		}
	}
}
