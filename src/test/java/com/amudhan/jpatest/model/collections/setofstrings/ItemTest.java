package com.amudhan.jpatest.model.collections.setofstrings;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;


public class ItemTest extends AbstractItemTest{
	
	private long persistedItemId;
	
	@Test(priority = 1)
	@Transactional
	@Commit
	public void setOfStringImagesInsert(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().add(RandomStringUtils.randomAlphabetic(5));
		entityManager.persist(item);
		persistedItemId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void setOfStringImagesTest(){
		Item persistedItem = entityManager.find(Item.class, persistedItemId);
		logger.info("Item ID: "+persistedItem.getId()+" Item name:"+persistedItem.getItemName());
		for(String image : persistedItem.getImages()){
			logger.info("Image file name :"+image);
		} 
	}
	
}
