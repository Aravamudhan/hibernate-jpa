package com.amudhan.jpatest.model.associations.onetomany.cascaderemove;

import java.math.BigDecimal;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {
	private long persistedItemId;

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToManyCascadePersistItemInsert(){
		Item item = new Item();
		item.setItemName("Random item name");
		item.getBids().add(new Bid(new BigDecimal(500), item));
		item.getBids().add(new Bid(new BigDecimal(400), item));
		item.getBids().add(new Bid(new BigDecimal(300), item));
		item.getBids().add(new Bid(new BigDecimal(200), item));
		entityManager.persist(item);
		persistedItemId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	@Commit
	public void oneToManyCascadePersistItemRemove(){
		Item item = entityManager.find(Item.class, persistedItemId);
		if(item != null){
			logger.info("Removing the item");
			/* The item#bids variable is marked for CascadeType.PERSIST and CascadeType.REMOVE. 
			 * This triggers persistence of bids, when the item is persisted and the removal when the item is removed.
			 * */
			entityManager.remove(item);
		}
	}

}
