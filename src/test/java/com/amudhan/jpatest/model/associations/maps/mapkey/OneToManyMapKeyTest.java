package com.amudhan.jpatest.model.associations.maps.mapkey;

import java.math.BigDecimal;
import java.util.Map.Entry;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToManyMapKeyTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	void oneToManyMapKeyInsert(){
		Item item = new Item("ItemOne");
		Bid bid = new Bid(new BigDecimal(1000), item);
		entityManager.persist(item);
		entityManager.persist(bid);
		persistedId = item.getId();
	}
	
	@Test(priority =2)
	@Transactional
	void oneToManyMapKeyDisplay(){
		Item item = entityManager.find(Item.class, persistedId);
		logger.info(item.toString());
		for(Entry<Long, Bid> bidMap : item.getBids().entrySet()){
			logger.info("ID :"+bidMap.getKey()+" Bid info "+bidMap.getValue());
		}
	}
}

