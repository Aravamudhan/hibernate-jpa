package com.amudhan.jpatest.model.associations.onetomany.direct;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_ONETOMANY_ITEM")
@Table(name = "ASSOCIATIONS_ONETOMANY_ITEM")
public class Item extends AbstractItem {

	@ManyToOne
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
