package com.amudhan.jpatest.model.associations.onetomany.cascadepersist;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {

	@Test
	@Transactional
	public void oneToManyCascadePersistItemInsert(){
		Item item = new Item();
		item.getBids().add(new Bid(new BigDecimal(500), item));
		item.getBids().add(new Bid(new BigDecimal(400), item));
		item.getBids().add(new Bid(new BigDecimal(300), item));
		item.getBids().add(new Bid(new BigDecimal(200), item));
	}
}
