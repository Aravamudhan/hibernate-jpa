package com.amudhan.jpatest.model.filtering.dynamic;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_DYNAMIC_CATEGORY")
public class Category {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    private String name;
	
	@OneToMany(mappedBy = "category")
	@org.hibernate.annotations.Filter(
	        name = "limitByUserRank",
	        condition =
	            ":currentUserRank >= (" +
	                    "select u.RANK from USERS u " +
	                    "where u.ID = SELLER_ID" +
	                ")"
	    )
	private Set<Item> items = new HashSet<Item>();
	
	public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}
}
