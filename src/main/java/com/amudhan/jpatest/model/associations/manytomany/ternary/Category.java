package com.amudhan.jpatest.model.associations.manytomany.ternary;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity(name = "ASSOCIATIONS_MANYTOMANY_TERNARY_CATEGORY")
@Table(name = "ASSOCIATIONS_MANYTOMANY_TERNARY_CATEGORY")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String categoryName;
	
	@ElementCollection
	@CollectionTable(
				name = "MANYTOMANY_TERNARY_CATEGORY_ITEM",
				joinColumns = @JoinColumn(name = "CATEGORY_ID")
			)
	private Set<CategorizedItem> categorizedItems = new HashSet<CategorizedItem>();

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

	public Set<CategorizedItem> getCategorizedItems() {
		return categorizedItems;
	}

	public void setCategorizedItems(Set<CategorizedItem> categorizedItems) {
		this.categorizedItems = categorizedItems;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", categoryName=" + categoryName + "]";
	}
	

}
