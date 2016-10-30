package com.amudhan.jpatest.model.filtering.cascade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name="FILTERING_CASCADE_ITEM")
public class Item {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	@NotNull
	private String name;
	
	@OneToMany(
			mappedBy = "item",
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
			)
	private Set<Bid> bids = new HashSet<Bid>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELLER_ID", nullable = false)
	@org.hibernate.annotations.Cascade(
	        org.hibernate.annotations.CascadeType.REPLICATE
	    )
	private User seller;
	
	/*
	 * In the default mapping of OneToMany without ManyToOne,
	 * a new join table which would contain the Item id and the
	 * related Image id. This join table will be the source of 
	 * this relationship. But, with the ManyToOne on the other side
	 * with the mappedBy here, no need for this join table. 
	 */
	@OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
	private Set<Image> images = new HashSet<Image>();
	
	public Item(){}
	
	public Item(String name, User seller){
		this.name = name;
		this.seller = seller;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
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

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
	
}

