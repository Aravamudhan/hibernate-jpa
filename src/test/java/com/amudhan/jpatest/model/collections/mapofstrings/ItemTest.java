package com.amudhan.jpatest.model.collections.mapofstrings;

import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.SuperItemTest;

public class ItemTest extends SuperItemTest {
	
	@Test
	@Transactional
	public void getItem(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getImages().put("Random1 file name", "Awesome1 image name");
		item.getImages().put("Random2 file name", "Awesome2 image name");
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(Entry<String, String> image: persistedItem.getImages().entrySet()){
			logger.info("Image details: "+image.getKey()+":"+image.getValue());
		}
	}
}
