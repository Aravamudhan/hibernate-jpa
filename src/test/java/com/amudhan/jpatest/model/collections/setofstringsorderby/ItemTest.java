package com.amudhan.jpatest.model.collections.setofstringsorderby;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {
	
	/*Priorities with lower number are scheduled first.*/
	@Test(priority=1)
	@Transactional
	/*Committing since the record is fetched in the next test to check the order.*/
	@Commit
	public void getItems(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().add(RandomStringUtils.randomAlphabetic(3));
		item.getImages().add(RandomStringUtils.randomAlphabetic(3));
		item.getImages().add(RandomStringUtils.randomAlphabetic(3));
		item.getImages().add(RandomStringUtils.randomAlphabetic(3));
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(String image: persistedItem.getImages()){
			logger.info("Image details: "+image);
		}
	}
	
	@Test(priority=2)
	@Transactional
	public void getItem(){
		Item persistedItem = entityManager.find(Item.class, new Long(1));
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(String image: persistedItem.getImages()){
			logger.info("Image details: "+image);
		}
	}
}
