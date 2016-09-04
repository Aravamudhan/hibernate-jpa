package com.amudhan.jpatest.model.associations.onetomany.cascadepersist;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_ONETOMANY_CASCADEPERSIST_ITEM")
@Table(name = "ASSOCIATIONS_ONETOMANY_CASCADEPERSIST_ITEM")
/*item#bids is saved along with the item.*/
public class Item extends AbstractItem
{
	@OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
	private Set<Bid> bids = new HashSet<Bid>();

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

}
