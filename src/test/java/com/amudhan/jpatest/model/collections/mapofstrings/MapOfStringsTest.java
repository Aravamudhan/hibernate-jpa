package com.amudhan.jpatest.model.collections.mapofstrings;

import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class MapOfStringsTest extends AbstractItemTest {
	
	@Test
	@Transactional
	public void getItem(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().put(RandomStringUtils.randomAlphabetic(3), "BImage");
		item.getImages().put(RandomStringUtils.randomAlphabetic(3), "AImage");
		item.getImages().put(RandomStringUtils.randomAlphabetic(3), "DImage");
		item.getImages().put(RandomStringUtils.randomAlphabetic(3), "ZImage");
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(Entry<String, String> image: persistedItem.getImages().entrySet()){
			logger.info("Image details: "+image.getKey()+":"+image.getValue());
		}
	}
}
