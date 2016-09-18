package com.amudhan.jpatest.model.associations.onetomany.jointable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_ONETOMANY_JOINTABLE_ITEM")
@Table(name = "ASSOCIATIONS_ONETOMANY_JOINTABLE_ITEM")
public class Item extends AbstractItem {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(
			name = "ITEM_BUYER",
			joinColumns =
				@JoinColumn(name = "ITEM_ID"),//defaults to ID
			inverseJoinColumns = 
				@JoinColumn(nullable = false) // defaults to BUYER_ID
			)
	private User buyer;

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + "]";
	}
	
}
