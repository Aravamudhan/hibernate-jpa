package com.amudhan.jpatest.model.associations.onetomany.orphanremoval;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractBid;

@Entity(name = "ASSOCIATIONS_ONETOMANY_ORPHANREMOVAL_BID")
@Table(name = "ASSOCIATIONS_ONETOMANY_ORPHANREMOVAL_BID")
public class Bid extends AbstractBid {
	
	public Bid(){}
	
	public Bid(BigDecimal amount, Item item){
		this.amount = amount;
		this.item = item;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private Item item;
	
	public Item getItem() {
		return item;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bid other = (Bid) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
