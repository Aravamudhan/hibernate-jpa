package com.amudhan.jpatest.model.associations.onetomany.cascaderemove;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_ONETOMANY_CASCADEREMOVE_ITEM")
@Table(name = "ASSOCIATIONS_ONETOMANY_CASCADEREMOVE_ITEM")
/*item#bids is saved along with the item, and removed along with the item too.*/
public class Item extends AbstractItem
{
	@OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<Bid> bids = new HashSet<Bid>();

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

}
