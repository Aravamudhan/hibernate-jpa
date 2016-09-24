package com.amudhan.jpatest.model.associations.manytomany.ternary;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

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
		
		/* This means that the userOne has added itemOne to categoryOne and categoryTwo*/
		CategorizedItem linkOne = new CategorizedItem(userOne, itemOne, new Date());
		CategorizedItem linkTwo = new CategorizedItem(userOne, itemTwo, new Date());
		/* Here is where the link table is created. CategorizedItem is embedded with Category.
		 * When it is added to a Category entity, it is automatically persisted.*/
		/* If equality methods were defined based on user(addedBy) and addedOn the following two
		 * methods calls would only add one CategorizedItem object since user is same in both the cases.*/
		categoryOne.getCategorizedItems().add(linkOne);
		categoryOne.getCategorizedItems().add(linkTwo);
		categoryTwo.getCategorizedItems().add(linkOne);
		
		categoryOneId = categoryOne.getId();
		categoryTwoId = categoryTwo.getId();
		itemOneId = itemOne.getId();
		itemTwoId = itemTwo.getId();
	}
	
	@SuppressWarnings("unchecked")
	@Test(priority = 2)
	@Transactional
	public void manyToManyTernaryDisplay(){
		logger.info("-------Category One--------");
		Category categoryOne = entityManager.find(Category.class, categoryOneId);
		logger.info(categoryOne.toString());
		logger.info("-------Items connected with Category One--------");
		for(CategorizedItem categorizedItem : categoryOne.getCategorizedItems()){
			logger.info(categorizedItem.toString());
			logger.info(categorizedItem.getItem().toString());
		}
		logger.info("-------Category Two--------");
		Category categoryTwo = entityManager.find(Category.class, categoryTwoId);
		logger.info(categoryTwo.toString());
		for(CategorizedItem categorizedItem : categoryTwo.getCategorizedItems()){
			logger.info(categorizedItem.getItem().toString());
		}
		/* Write JPQL with care. It is case sensitive since it deals with entities.
		 * The properties are the class properties. The names should match.*/
		Query query = entityManager.createQuery("SELECT c from ASSOCIATIONS_MANYTOMANY_TERNARY_CATEGORY c join "
				+ "c.categorizedItems ci where ci.item = :itemParameter");
		logger.info("-------Item One------------");
		Item itemOne = entityManager.find(Item.class, itemOneId);
		logger.info(itemOne.toString());
		/* Though Categories to Items is not bidirectional, the entities in this relationship
		 * can be listed just by having an item and writing an appropriate query to the Category table.*/
		List<Category> categoriesOfItemOne = query.setParameter("itemParameter", itemOne).getResultList();
		for(Category category : categoriesOfItemOne){
			logger.info(category.toString());
		}
		logger.info("-------Item Two------------");
		Item itemTwo = entityManager.find(Item.class, itemTwoId);
		logger.info(itemTwo.toString());
		List<Category> categoriesOfItemTwo = query.setParameter("itemParameter", itemTwo).getResultList();
		for(Category category : categoriesOfItemTwo){
			logger.info(category.toString());
		}
		
	}
}


