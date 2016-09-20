package com.amudhan.jpatest.model.associations.manytomany.bidirectional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "ASSOCIATIONS_MANYTOMANY_BIDIRECTIONAL_ITEM")
@Table(name = "ASSOCIATIONS_MANYTOMANY_BIDIRECTIONAL_ITEM")
public class Item extends AbstractItem {

	@ManyToMany(mappedBy = "items")
	Set<Category> categories = new HashSet<Category>();
	
	public Item(){}
	
	public Item(String itemName){
		this.itemName = itemName;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + "]";
	}
	
}
