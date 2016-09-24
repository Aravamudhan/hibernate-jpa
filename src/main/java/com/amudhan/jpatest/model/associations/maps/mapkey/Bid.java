package com.amudhan.jpatest.model.associations.maps.mapkey;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractBid;

@Entity(name= "ASSOCIATIONS_MAPS_MAPKEY_BID")
@Table(name= "ASSOCIATIONS_MAPS_MAPKEY_BID")
public class Bid extends AbstractBid {

	@ManyToOne
	private Item item;
	
	public Bid(){}
	
	public Bid(BigDecimal amount, Item item){
		this.amount = amount;
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Bid [id=" + id + ", amount=" + amount + "]";
	}

}
