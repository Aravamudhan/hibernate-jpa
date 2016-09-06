package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

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
	public void oneToManyBiItemInsert(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		Bid bid = new Bid(new BigDecimal(ThreadLocalRandom.current().nextInt(100, 500)), item);
		item.getBids().add(bid);
		entityManager.persist(item);
		/* With no cascade option set, explicitly the bid must be persisted.
		 * Also, the order is of highest importance. The bid must be persisted after the item.
		 * If order is changed, it would throw an exception with the message, "Not null property references a transient value."
		 * The reason for this is, the item remains transient until after the persist is called on that.
		 * */
		entityManager.persist(bid);
		persistedItemId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToManyBiItemTest(){
		Item persistedItem = entityManager.find(Item.class, persistedItemId);
		logger.info("Item ID: "+persistedItem.getId()+" Item Name: "+persistedItem.getItemName());
		for(Bid persistedBid : persistedItem.getBids()){
			logger.info("Bid ID: "+persistedBid.getId()+" Bid amount:"+persistedBid.getAmount());
		}
	}
	
	@Test(priority = 3)
	@Transactional
	public void oneToManyBiItemDelete(){
		Item persistedItem = entityManager.find(Item.class, persistedItemId);
		for(Bid persistedBid : persistedItem.getBids()){
			logger.info("Bid details: "+persistedBid);
		}
	}
}
