package com.amudhan.jpatest.model.filtering.envers;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_ENVERS_ITEM")
/*
 * This entire entity becomes part of the related audit table.
 * When ever an item entity is created, all the data in that
 * entity except those in the fields marked with NotAudited,
 * are inserted into a specific audit table created for that entity.
 * 
 */
@org.hibernate.envers.Audited
public class Item {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	@NotNull
	private String name;
	
	@OneToMany(mappedBy = "item")
	@org.hibernate.envers.NotAudited
	private Set<Bid> bids = new HashSet<Bid>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SELLER_ID", nullable=false)
	private User seller;
	
	protected Item() {
    }

    public Item(String name, User seller) {
        this.name = name;
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

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
    
}
