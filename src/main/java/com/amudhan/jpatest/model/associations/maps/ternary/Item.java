package com.amudhan.jpatest.model.associations.maps.ternary;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_MAPS_TERNARY_ITEM")
@Table(name = "ASSOCIATIONS_MAPS_TERNARY_ITEM")
public class Item extends AbstractItem {

	public Item(){}
	
	public Item(String itemName){
		this.itemName = itemName;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + "]";
	}
	
}
