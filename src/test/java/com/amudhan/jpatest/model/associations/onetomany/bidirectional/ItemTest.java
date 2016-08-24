package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:configuration/applicationContext-test.xml")
public class ItemTest extends AbstractTransactionalTestNGSpringContextTests{
	@PersistenceContext
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(ItemTest.class);
	
	@Test
	@Transactional
	public void getItems(){
		Item item = new Item();
		item.setItemName(RandomStringUtils.randomAlphabetic(10));
		item.getBids().add(new Bid(new BigDecimal(ThreadLocalRandom.current().nextInt(100, 500))));
		entityManager.persist(item);
		entityManager.flush();
		Item persistedItem = entityManager.find(Item.class, item.getId());
		logger.info(persistedItem.getId()+" "+persistedItem.getItemName());
		for(Bid persistedBid : persistedItem.getBids()){
			logger.info("Bid ID: "+persistedBid.getId()+" Bid amount:"+persistedBid.getAmount());//TODO:Bid#id is 0. check
		}
	}
}
