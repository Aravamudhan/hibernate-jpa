package com.amudhan.jpatest.model.fetching.nplusoneselects;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "FETCHING_NPLUSONESELECTS_BID")
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private User bidder;
	
	@NotNull
	private BigDecimal amount;

	public Bid() {
	}

	public Bid(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Bid(BigDecimal amount, Item item, User bidder) {
		this.amount = amount;
		this.item = item;
		this.bidder = bidder;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

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

	public Long getId() {
		return id;
	}

}
