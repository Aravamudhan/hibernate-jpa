package com.amudhan.jpatest.model.associations.onetoone.jointable;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToOneJoinTableTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToOneJoinTableInsert(){
		Shipment shipment = new Shipment();
		entityManager.persist(shipment);
		Item auctionedItem = new Item("Fantastic Item");
		entityManager.persist(auctionedItem);
		Shipment auctionShipment = new Shipment(auctionedItem);
		entityManager.persist(auctionShipment);
		persistedId = auctionShipment.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToOneJoinTablePrint(){
		Shipment shipment = entityManager.find(Shipment.class, persistedId);
		logger.info("Shipment id: "+shipment.getId());
		logger.info("Item id: "+shipment.getAuctionedItem().getId());
		logger.info("Item name: "+shipment.getAuctionedItem().getItemName());
	}
}
