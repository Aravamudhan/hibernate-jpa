package com.amudhan.jpatest.model.associations.onetomany.orphanremoval;

import java.math.BigDecimal;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.amudhan.jpatest.model.AbstractBid;

public class Bid extends AbstractBid {
	
	public Bid(){}
	
	public Bid(BigDecimal amount, Item item){
		this.amount = amount;
		this.item = item;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Item item;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User bidder;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}
	
}
