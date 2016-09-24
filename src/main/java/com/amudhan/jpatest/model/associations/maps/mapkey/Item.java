package com.amudhan.jpatest.model.associations.maps.mapkey;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name= "ASSOCIATIONS_MAPS_MAPKEY_ITEM")
@Table(name= "ASSOCIATIONS_MAPS_MAPKEY_ITEM")
public class Item extends AbstractItem {

	/* Motivation is this for different interpretation of data.*/
	/* When data is loaded from the data source, the id is loaded to the key.
	 * It can be changed to any other column.*/
	@MapKey(name = "id")
	@OneToMany(mappedBy = "item",fetch = FetchType.EAGER)
	private Map<Long, Bid> bids = new HashMap<>();

	public Item(){}
	
	public Item(String itemName){
		this.itemName = itemName;
	}
	public Map<Long, Bid> getBids() {
		return bids;
	}

	public void setBids(Map<Long, Bid> bids) {
		this.bids = bids;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + "]";
	}
}
