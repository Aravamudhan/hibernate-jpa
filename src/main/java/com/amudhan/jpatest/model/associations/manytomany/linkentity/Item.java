package com.amudhan.jpatest.model.associations.manytomany.linkentity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_MANYTOMANY_LINKENTITY_ITEM")
@Table(name = "ASSOCIATIONS_MANYTOMANY_LINKENTITY_ITEM")
public class Item extends AbstractItem {

	@OneToMany(mappedBy = "item")
	Set<CategorizedItem> categorizedItems = new HashSet<CategorizedItem>();
	
	public Item(){}
	
	public Item(String itemName){
		this.itemName = itemName;
	}

	public Set<CategorizedItem> getCategorizedItems() {
		return categorizedItems;
	}

	public void setCategorizedItems(Set<CategorizedItem> categorizedItems) {
		this.categorizedItems = categorizedItems;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + "]";
	}
	
}
