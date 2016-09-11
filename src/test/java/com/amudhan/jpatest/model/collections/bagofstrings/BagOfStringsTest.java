package com.amudhan.jpatest.model.collections.bagofstrings;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class BagOfStringsTest extends AbstractItemTest {
	
	@Test
	@Transactional
	public void getItem(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().add(RandomStringUtils.randomAlphabetic(10));
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(String image : persistedItem.getImages()){
			logger.info("Image name "+image);
		}
	}
}
