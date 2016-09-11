package com.amudhan.jpatest.model.associations.onetomany.orphanremoval;

import java.math.BigDecimal;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OrphanRemovalTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void itemPersist(){
		Item item = new Item();
		item.setItemName("ParentItem");
		Bid bidOne = new Bid(new BigDecimal(100), item);
		item.getBids().add(bidOne);
		entityManager.persist(item);
		persistedId = item.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	@Commit
	public void bidsClear(){
		Item item = entityManager.find(Item.class, persistedId);
		/* Since, orphanRemoval is set, this will trigger delete of all the bids,
		 * which have this item as their parent.*/
		item.getBids().clear();
	}

}
