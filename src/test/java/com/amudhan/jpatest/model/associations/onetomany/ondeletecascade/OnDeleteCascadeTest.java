package com.amudhan.jpatest.model.associations.onetomany.ondeletecascade;

import java.math.BigDecimal;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OnDeleteCascadeTest extends AbstractItemTest {

	private long bidId;
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
		persistedId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	@Commit
	public void oneToManyCascadePersistItemRemove(){
		Item item = entityManager.find(Item.class, persistedId);
		bidId = item.getBids().iterator().next().getId();
		if(item != null){
			logger.info("Removing the item");
			entityManager.remove(item);
		}
	}

	@Test(priority = 3)
	@Transactional
	public void getBid(){
		Bid bid = entityManager.find(Bid.class, bidId);
		logger.info("Does the bid with the id "+bidId+" exist ?"+(bid!=null));
	}
}
