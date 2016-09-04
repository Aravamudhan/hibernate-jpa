package com.amudhan.jpatest.model.associations.onetomany.cascadepersist;

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
		/* 
		 * The item is not set for the testBid below.
		 * This would throw an error from the data source with 'Null not allowed' error message.
		 * The reason for that is joinColumn of ITEM_ID has been over ridden from the Bid with 'nullable = false'.
		 * If this is not over ridden, there would be no error. But the testBid would have been orphan in that case.
		 * Bids are dependent upon Items. Bids must not exists except along with the existence of a related item.
		 * The nullable = false enforces this contract.
		 * 
		*/
		/*Bid testBid = new Bid();
		testBid.setAmount(new BigDecimal(5656));
		item.getBids().add(testBid);*/
		
		item.getBids().add(new Bid(new BigDecimal(500), item));
		item.getBids().add(new Bid(new BigDecimal(400), item));
		item.getBids().add(new Bid(new BigDecimal(300), item));
		item.getBids().add(new Bid(new BigDecimal(200), item));
		entityManager.persist(item);
		persistedItemId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToManyCascadePersistItemTest(){
		Item item = entityManager.find(Item.class, persistedItemId);
		if(item!=null){
			logger.info("Item ID: "+item.getId()+" Item name "+item.getItemName());
			for(Bid bid : item.getBids()){
				logger.info("Bid"+bid);
			}
		}else{
			logger.info("Item with the ID 1 does not exist");
		}
	}
}
