package com.amudhan.jpatest.model.collections.setofembeddables;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void setOfEmbeddableImagesInsert() {
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(5));
		item.getImages().add(
				new Image(RandomStringUtils.randomAlphabetic(5),
						RandomStringUtils.randomAlphabetic(5), RandomUtils
								.nextInt(100, 200), RandomUtils.nextInt(100,
								200)));
		/* Since two objects have the same properties, only one will be added. 
		 * Without equals and hash code override both will be added to the Set.*/
		item.getImages().add(new Image("Baz", "baz.jpg", 100, 200));
		item.getImages().add(new Image("Baz", "baz.jpg", 100, 200));
		item.getImages().add(new Image("Faz", "faz.jpg", 200, 220));
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
	public void setOfEmbeddableImagesTest() {
		Item item = entityManager.find(Item.class, new Long(1));
		logger.info("Item ID: " + item.getId() + " Item name:"
				+ item.getItemName());
		for (Image image : item.getImages()) {
			logger.info(image.toString());
		}
	}

}
