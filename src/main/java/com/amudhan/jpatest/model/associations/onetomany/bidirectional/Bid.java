package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractBid;

@Entity(name="ASSOCIATIONS_ONETOMANY_BI_BID")
@Table(name="ASSOCIATIONS_ONETOMANY_BI_BID")
public class Bid extends AbstractBid{
	
	@ManyToOne(fetch = FetchType.LAZY)//Default is EAGER
	/* By default, the identifier of the entity that is mapped as ManyToOne, will be used as the join column.
	 * In this case, a column with the name "item_id" will be added to the Bid table, and would be mapped as a foreign key.
	 * Here it is over ridden to make it not null in the schema By default, this join column is nullable.*/
	@JoinColumn(name = "ITEM_ID", nullable = false)
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
	
	
}
