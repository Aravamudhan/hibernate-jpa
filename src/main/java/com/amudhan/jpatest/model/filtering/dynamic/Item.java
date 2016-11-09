package com.amudhan.jpatest.model.filtering.dynamic;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_DYNAMIC_ITEM")
@org.hibernate.annotations.Filter(
	    name = "limitByUserRank",
	    condition =
	        ":currentUserRank >= (" +
	                "select u.RANK from USERS u " +
	                "where u.ID = SELLER_ID" +
	            ")"
)	                
public class Item {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User seller;

    @SuppressWarnings("unused")
	private Item() {
    }

    public Item(String name, Category category, User seller) {
        this.name = name;
        this.category = category;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public User getSeller() {
        return seller;
    }

}
