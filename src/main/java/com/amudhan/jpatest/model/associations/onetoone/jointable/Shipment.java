package com.amudhan.jpatest.model.associations.onetoone.jointable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "ASSOCIATIONS_ONETOONE_JOINTABLE_SHIPMENT")
@Table(name = "ASSOCIATIONS_ONETOONE_JOINTABLE_SHIPMENT")
public class Shipment {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(
			name = "ITEM_SHIPMENT",
			joinColumns = @JoinColumn (name = "SHIPMENT_ID"),
			inverseJoinColumns = @JoinColumn(name = "ITEM_ID", nullable = false, unique = true)
			)
	private Item auctionedItem;
	
	public Shipment(){}
	
	public Shipment (Item auctionedItem){
		this.auctionedItem = auctionedItem;
	}

	public Item getAuctionedItem() {
		return auctionedItem;
	}

	public void setAuctionedItem(Item auctionedItem) {
		this.auctionedItem = auctionedItem;
	}
}
