package com.amudhan.jpatest.model.concurrency.version;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "CONCURRENCY_VERSION_BID")
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private BigDecimal amount;

	@ManyToOne(optional= false, fetch = FetchType.LAZY)
	private Item item;

	public Bid() {
	}

	public Bid(BigDecimal amount, Item item) {
		this.amount = amount;
		this.item = item;
	}
	
	public Bid(BigDecimal amount, Item item, Bid lastBid) throws InvalidBidException {
		if (lastBid != null && amount.compareTo(lastBid.getAmount()) < 1) {
            throw new InvalidBidException(
                "Bid amount '" + amount +" too low, last bid was: " + lastBid.getAmount()
            );
        }
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
