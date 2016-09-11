package com.amudhan.jpatest.model.collections.mapofstringembeddables;

import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class MapOfStringEmbeddablesTest extends AbstractItemTest {
	
	@Test
	@Transactional
	public void mapOfStringEmdebbaleImageInsert(){
		Item item = new Item();
		item.setItemName("Random file name");
		item.getImages().put("Fizz.jpg", new Image("The fizz image",100,200));
		item.getImages().put("Buzz.jpg", new Image("The buzz image",100,200));
		item.getImages().put("FizzBuzz.jpg", new Image("The fizzbuzz image",100,200));
		entityManager.persist(item);
		entityManager.flush();
		logger.info("Item ID: "+item.getId()+" Item name:"+item.getItemName());
		for(Entry<String, Image> image: item.getImages().entrySet()){
			logger.info(image.getValue()+" File name :"+image.getKey());
		}
	}

}
