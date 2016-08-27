package com.amudhan.jpatest.model.collections.listofstrings;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {
	
	@Test
	@Transactional
	public void getItems(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().add(RandomStringUtils.randomAlphabetic(5));
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info("Item ID: "+persistedItem.getId()+" Item name:"+persistedItem.getItemName());
		for(String image : persistedItem.getImages()){
			logger.info("Image file name :"+image);
		}
	}
	@Test
	@Transactional
	public void deleteImage(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().add(RandomStringUtils.randomAlphabetic(5));
		item.getImages().add(RandomStringUtils.randomAlphabetic(5));
		item.getImages().add(RandomStringUtils.randomAlphabetic(5));
		item.getImages().add(RandomStringUtils.randomAlphabetic(5));
		entityManager.persist(item);
		entityManager.flush();
		logger.info("Before delete");
		for(String image : item.getImages()){
			logger.info("Image "+image);
		}
		item.getImages().remove(1);
		/*This fires one delete query and two update queries.*/
		entityManager.persist(item);
		entityManager.flush();
		logger.info("After delete");
		for(String image : item.getImages()){
			logger.info("Image "+image);
		}
	}
	
}
