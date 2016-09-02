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
}
