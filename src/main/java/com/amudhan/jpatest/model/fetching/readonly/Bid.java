package com.amudhan.jpatest.model.fetching.readonly;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "FETCHING_READONLY_BID")
@org.hibernate.annotations.Immutable
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private User bidder;
	
	@NotNull
	private BigDecimal amount;
	
	public Bid(){}
	
	public Bid(Item item, User bidder, BigDecimal amount){
		this.item = item;
		this.bidder = bidder;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
