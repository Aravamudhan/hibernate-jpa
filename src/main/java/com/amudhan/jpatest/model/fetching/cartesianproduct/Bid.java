package com.amudhan.jpatest.model.fetching.cartesianproduct;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "FETCHING_CARTESIANPRODUCT_BID")
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
	@NotNull
	private BigDecimal amount;

	public Bid() {
	}

	public Bid(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Bid(BigDecimal amount, Item item) {
		this.amount = amount;
		this.item = item;
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

	public Long getId() {
		return id;
	}

}
