package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="ASSOCIATIONS_ONETOMANY_BI_BID")
@Table(name="ASSOCIATIONS_ONETOMANY_BI_BID")
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@ManyToOne(fetch = FetchType.LAZY)//Default is EAGER
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private Item item;

	public Bid(){}
	
	public Bid(BigDecimal amount){
		this.amount = amount;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	
}
