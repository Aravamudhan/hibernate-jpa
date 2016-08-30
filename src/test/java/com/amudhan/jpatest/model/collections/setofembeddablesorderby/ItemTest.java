package com.amudhan.jpatest.model.collections.setofembeddablesorderby;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {

	@Test( priority = 1)
	@Transactional
	@Commit
	public void setOfOrderedEmbeddableImagesInsert(){
		Item item = new Item();
		item.setItemName("RandomItemName");
		item.getImages().add(new Image("Zaz", "Zaz.jpg", 50, 200));
		item.getImages().add(new Image("Aaz", "Aaz.jpg", 150, 220));
		item.getImages().add(new Image("aaz", "aaz.jpg", 90, 220));
		item.getImages().add(new Image("zaz", "zaz.jpg", 25, 820));
		item.getImages().add(new Image("baz", "baz.jpg", 45, 520));
		item.getImages().add(new Image("Baz", "Baz.jpg", 35, 420));
		item.getImages().add(new Image("Daz", "Daz.jpg", 65, 120));
		
		entityManager.persist(item);
		entityManager.flush();
		logger.info("Item ID: " + item.getId() + " Item name:"
				+ item.getItemName());
		for (Image image : item.getImages()) {
			logger.info(image.toString());
		}
	}
	
	@Test(priority = 2)
	@Transactional
	public void setOfOrderedEmbeddableImagesTest() {
		Item item = entityManager.find(Item.class, new Long(1));
		logger.info("Item ID: " + item.getId() + " Item name:"
				+ item.getItemName());
		for (Image image : item.getImages()) {
			logger.info(image.toString());
		}
	}

}
