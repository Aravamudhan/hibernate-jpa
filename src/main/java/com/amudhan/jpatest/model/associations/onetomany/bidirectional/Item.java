package com.amudhan.jpatest.model.associations.onetomany.bidirectional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="ASSOCIATIONS_ONETOMANY_BI_ITEM")
@Table(name="ASSOCIATIONS_ONETOMANY_BI_ITEM")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="ITEM_NAME")
	private String itemName;
	
	/*OneToMany is required here to access the other side of the relationship.
	 * This generates SELECT * FROM BID WHERE ITEM_ID = :ITEM_ID*/
	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
	private Set<Bid> bids = new HashSet<Bid>();//Always it is safer to initialize

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}
	
	
}
