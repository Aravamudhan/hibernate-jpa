package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest{
	
	@Test
	@Transactional
	public void getItems(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getBids().add(new Bid(new BigDecimal(ThreadLocalRandom.current().nextInt(100, 500)), item));
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(Bid persistedBid : persistedItem.getBids()){
			logger.info("Bid ID: "+persistedBid.getId()+" Bid amount:"+persistedBid.getAmount());
		}
	}
}
