package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name="ASSOCIATIONS_ONETOMANY_BI_ITEM")
@Table(name="ASSOCIATIONS_ONETOMANY_BI_ITEM")
public class Item extends AbstractItem{
	
	/*OneToMany is required here to access the other side of the relationship.
	 * This generates SELECT * FROM BID WHERE ITEM_ID = :ITEM_ID*/
	/* In a bidirectional relationship, one side must be mapped by other side.
	 * This means, it's mapping settings are ignored and the other side's
	 * settings take precedence.*/
	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
	private Set<Bid> bids = new HashSet<Bid>();//Always it is safer to initialize

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}
	
	
}
