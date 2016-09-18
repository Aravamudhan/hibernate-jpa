package com.amudhan.jpatest.model.associations.onetomany.list;

import java.math.BigDecimal;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToManyListTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToManyListInsert(){
		Item item = new Item();
		Bid bidOne = new Bid(new BigDecimal(1000), item);
		Bid bidTwo = new Bid(new BigDecimal(2000), item);
		Bid bidThree = new Bid(new BigDecimal(3000), item);
		item.setItemName("An item name");
		/* The order will be maintained when the bids are fetched.
		 * It does not matter in which order the bids are persisted,
		 * but the order in which they are added.*/
		item.getBids().add(bidThree);
		item.getBids().add(bidOne);
		item.getBids().add(bidTwo);
		entityManager.persist(item);
		entityManager.persist(bidOne);
		entityManager.persist(bidThree);
		entityManager.persist(bidTwo);
		persistedId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToManyListDisplay(){
		Item item = entityManager.find(Item.class, persistedId);
		logger.info(item.toString());
		/* Here the order will be the one that bids are added to the item.*/
		for(Bid bid : item.getBids()){
			logger.info(bid.toString());
		}
	}
}
