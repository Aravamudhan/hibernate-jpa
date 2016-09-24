package com.amudhan.jpatest.model.associations.maps.ternary;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "ASSOCIATIONS_MAPS_TERNARY_CATEGORY")
@Table(name = "ASSOCIATIONS_MAPS_TERNARY_CATEGORY")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String categoryName;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@MapKeyJoinColumn(name = "ITEM_ID")
	/* This table stores CATEGORY_ID, USER_ID and ITEM_ID.
	 * This is another way to represent data. This does not offer any 
	 * advantages over link table or CollectionTable.
	 * This simply is a way of accessing data.*/
	@JoinTable(
				name = "MAPS_CATEGORY_ITEM",
				joinColumns = @JoinColumn(name = "CATEGORY_ID"),
				inverseJoinColumns = @JoinColumn(name = "USER_ID")
			)
	private Map<Item, User> itemAddedBy = new HashMap<>();
	
	public Category(){}
	
	public Category(String categoryName){
		this.categoryName = categoryName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Map<Item, User> getItemAddedBy() {
		return itemAddedBy;
	}

	public void setItemAddedBy(Map<Item, User> itemAddedBy) {
		this.itemAddedBy = itemAddedBy;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", categoryName=" + categoryName + "]";
	}
}
