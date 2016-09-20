package com.amudhan.jpatest.model.associations.manytomany.bidirectional;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity(name = "ASSOCIATIONS_MANYTOMANY_BIDIRECTIONAL_CATEGORY")
@Table(name = "ASSOCIATIONS_MANYTOMANY_BIDIRECTIONAL_CATEGORY")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String categoryName;
	
	/* Apart from Persist, other options do not make sense.*/
	@ManyToMany(cascade = CascadeType.PERSIST)
	/* Link table creation*/
	@JoinTable(
			name = "CATEGORY_ITEM",
			/* The primary key is the combination of both the keys.*/
			joinColumns = @JoinColumn(name = "CATEGORY_ID"),
			inverseJoinColumns = @JoinColumn(name = "ITEM_ID")
		)
	private Set<Item> items = new HashSet<Item>();

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

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", categoryName=" + categoryName + "]";
	}
	

}
