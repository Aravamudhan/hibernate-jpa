package com.amudhan.jpatest.model.associations.onetomany.list;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name="ASSOCIATIONS_ONETOMANY_LIST_ITEM")
@Table(name="ASSOCIATIONS_ONETOMANY_LIST_ITEM")
public class Item extends AbstractItem{
	
	@OneToMany
	@JoinColumn(
			/* Since the ManyToOne is readable here, this is required.
			 * The name should match with that of ManyToOne*/
			name = "ITEM_ID",
			nullable = false
			)
	/* This specifies the column that maintains ordering index.
	 * This can be configured at the collection side only.
	 * Hence at the OneToMany side, not at the ManyToOne side.
	 * Persistence provider(Hibernate here) takes care of updating
	 * the ordering information after flush.
	 * This is not visible as a part of the entity. */
	/* This type of configuration is useful only if we are sure that the
	 * ordering will not change based on any other criteria. Otherwise,
	 * this is expensive. In most of the cases it is better to  not to
	 * rely upon lists with index and write custom queries.*/
	@OrderColumn(
			name = "BID_POSITION", 
			nullable = false)
	private List<Bid> bids = new ArrayList<Bid>();

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + "]";
	}

}
