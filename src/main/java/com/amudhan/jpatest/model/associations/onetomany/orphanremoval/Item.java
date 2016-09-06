package com.amudhan.jpatest.model.associations.onetomany.orphanremoval;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import com.amudhan.jpatest.model.AbstractItem;

public class Item extends AbstractItem {
	
	@OneToMany(mappedBy = "item",
			   cascade = CascadeType.PERSIST,
			   /* orphanRemoval is more aggressive than CascadeType.REMOVE.
			    * If the CascadeType is REMOVE, bids are destroyed only when the dependent Item is removed.
			    * If item#bids is set to null, the bids are not removed.
			    * If the orphanRemoval is true and the item#bids is set to null, all the dependent bids are 
			    * removed.*/
			   orphanRemoval = true)
	private Set<Bid> bids = new HashSet<Bid>();

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

}
