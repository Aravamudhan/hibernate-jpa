package com.amudhan.jpatest.model.associations.onetomany.ondeletecascade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_ONETOMANY_ONDELETECASCADE_ITEM")
@Table(name = "ASSOCIATIONS_ONETOMANY_ONDELETECASCADE_ITEM")
public class Item extends AbstractItem
{
	@OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
	/* When schema is generated, ON CASCADE DELETE is added to the
	 * foreign key constraint ITEM_ID of the BID table.*/
	/* When an item is removed, bids that hold the foreign key of the item also are deleted.
	 * This happens without the knowledge of the hibernate and executed by the data base.
	 * */
	@org.hibernate.annotations.OnDelete(
			action = org.hibernate.annotations.OnDeleteAction.CASCADE
	)
	private Set<Bid> bids = new HashSet<Bid>();

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

}
