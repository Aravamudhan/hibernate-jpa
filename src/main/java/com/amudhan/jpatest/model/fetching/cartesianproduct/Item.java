package com.amudhan.jpatest.model.fetching.cartesianproduct;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name = "FETCHING_CARTESIANPRODUCT_ITEM")
public class Item {

	private Long id;

	private String name;

	private LocalDateTime auctionEnd;

	private User seller;

	private Set<Bid> bids = new HashSet<Bid>();
	
	private Set<String> images = new HashSet<String>();

	public Item() {
	}

	public Item(String name, LocalDateTime auctionEnd, User seller) {
		this.name = name;
		this.auctionEnd = auctionEnd;
		this.seller = seller;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	public LocalDateTime getAuctionEnd() {
		return auctionEnd;
	}

	public void setAuctionEnd(LocalDateTime auctionEnd) {
		this.auctionEnd = auctionEnd;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	@OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "FETCHING_CARTESIANPRODUCT_ITEM_IMAGE")
    @Column(name = "FILENAME")
	public Set<String> getImages() {
		return images;
	}

	public void setImages(Set<String> images) {
		this.images = images;
	}
	

}
