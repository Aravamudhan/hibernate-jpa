package com.amudhan.jpatest.model.associations.onetomany.bag;

import java.math.BigDecimal;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToManyBagTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToManyBagInsert(){
		Item item = new Item();
		Bid bidOne = new Bid(new BigDecimal(1000), item);
		Bid bidTwo = new Bid(new BigDecimal(2000), item);
		Bid bidThree = new Bid(new BigDecimal(3000), item);
		item.setItemName("Awesome Item");
		/* Note that no bid is added to the item entity.
		 * Even then, when item.getBids is called, in another transaction, it loads all the bids
		 * which have many to one relationship with this item.
		 * To enforce this, item#bids should be marked non optional .*/
		entityManager.persist(item);
		entityManager.persist(bidOne);
		entityManager.persist(bidTwo);
		entityManager.persist(bidThree);
		persistedId = item.getId();
		/* With in the same transaction this does not work.
		 * The bids must have been added to the item.*/
		/*for(Bid bid : item.getBids()){
			logger.info(bid.getId()+" "+bid.getAmount());
		}*/
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToManyBagDisplay(){
		Item item = entityManager.find(Item.class, persistedId);
		logger.info(item.getId()+" "+item.getItemName());
		/* This loads all the bids that have ManyToOne relationship with item.*/
		for(Bid bid : item.getBids()){
			logger.info(bid.getId()+" "+bid.getAmount());
		}
	}
}

