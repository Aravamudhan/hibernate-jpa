package com.amudhan.jpatest.model.associations.manytomany.ternary;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ManyToManyTernaryTest extends AbstractItemTest {

	private long categoryOneId;
	private long categoryTwoId;
	private long itemOneId;
	private long itemTwoId;
	
	@Test(priority = 1)
	@Transactional
	@Commit
	public void manyToManyTernaryInsert(){
		Category categoryOne = new Category("FirstCategory");
		Category categoryTwo = new Category("SecondCategory");
		entityManager.persist(categoryOne);
		entityManager.persist(categoryTwo);
		
		Item itemOne = new Item("FirstItem");
		Item itemTwo = new Item("SecondItem");
		entityManager.persist(itemOne);
		entityManager.persist(itemTwo);
		
		User userOne = new User();
		userOne.setName("UserOne");
		entityManager.persist(userOne);
		
		CategorizedItem linkOne = new CategorizedItem(userOne, itemOne);
		categoryOne.getCategorizedItems().add(linkOne);
		categoryTwo.getCategorizedItems().add(linkOne);
		
		categoryOneId = categoryOne.getId();
		categoryTwoId = categoryTwo.getId();
		itemOneId = itemOne.getId();
		itemTwoId = itemTwo.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void manyToManyTernaryDisplay(){
		logger.info("-------Category One--------");
		Category categoryOne = entityManager.find(Category.class, categoryOneId);
		logger.info(categoryOne.toString());
		for(CategorizedItem categorizedItem : categoryOne.getCategorizedItems()){
			logger.info(categorizedItem.toString());
		}
		logger.info("-------Category Two--------");
		Category categoryTwo = entityManager.find(Category.class, categoryTwoId);
		logger.info(categoryTwo.toString());
		for(CategorizedItem categorizedItem : categoryTwo.getCategorizedItems()){
			logger.info(categorizedItem.toString());
		}
	}
}


