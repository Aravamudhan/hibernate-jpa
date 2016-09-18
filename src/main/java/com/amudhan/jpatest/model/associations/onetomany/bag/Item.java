package com.amudhan.jpatest.model.associations.onetomany.bag;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name="ASSOCIATIONS_ONETOMANY_BAG_ITEM")
@Table(name="ASSOCIATIONS_ONETOMANY_BAG_ITEM")
public class Item extends AbstractItem{
	
	@OneToMany(mappedBy = "item")
	/* Bags are the most efficient of collections. They do not need to keep the elements unique like set.
	 * They do not need to maintain index like lists.*/
	private Collection<Bid> bids = new ArrayList<Bid>();

	public Collection<Bid> getBids() {
		return bids;
	}

	public void setBids(Collection<Bid> bids) {
		this.bids = bids;
	}

}
