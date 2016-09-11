package com.amudhan.jpatest.model.collections.mapofembeddables;

import java.util.Map.Entry;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class MapOfEmbeddablesTest extends AbstractItemTest {

	private long persistedId; 
	
	@Test(priority =1 )
	@Transactional
	@Commit 
	public void mapOfEmbeddableImageInsert(){
		Item item = new Item();
		item.setItemName("Random Item Name");
		item.getImages().put(new FileName("Fizz",".jpg"), new Image("Fizz file", 100, 150));
		item.getImages().put(new FileName("Buzz",".jpg"), new Image("Bzz file", 120, 250));
		item.getImages().put(new FileName("FizzBuzz",".jpg"), new Image("FizzBuzz file", 300, 350));
		entityManager.persist(item);
		persistedId = item.getId();
	}
	
	@Test(priority = 2 )
	@Transactional
	public void mapOfEmbeddableImageTest(){
		Item item = entityManager.find(Item.class, persistedId);
		logger.info("Item ID: "+item.getId()+" Item name:"+item.getItemName());
		for(Entry<FileName, Image> images: item.getImages().entrySet()){
			logger.info(images.getKey()+""+images.getValue());
		}
	}
}
