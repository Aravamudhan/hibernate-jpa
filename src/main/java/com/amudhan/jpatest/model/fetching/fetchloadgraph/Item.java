package com.amudhan.jpatest.model.fetching.fetchloadgraph;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/* 
 * NamedEntityGraphs give great control over loading entities.
 * By defining different entity graphs, certain entities can be
 * loaded eagerly and certain entities can be loaded lazily.
 * This is a way to tackle n+1 selection problem.
 */
/*
 * There are two graphs here, the 1st is the default graph
 * that loads an item with its graph with default fetching,
 * and another is ItemSeller which loads the Item#seller
 * eagerly.
 */
@NamedEntityGraphs({
	/*
	 * Without name this defaults to the entity's name.
	 * Without any attribute, this loads all the field
	 * with their default fetch plan/type.
	 */
		@NamedEntityGraph,
		@NamedEntityGraph(
				name = "ItemSeller", 
				attributeNodes = { 
						@NamedAttributeNode("seller") 
				}) 
		})
@Entity(name = "FETCHING_FETCHLOADGRAPH_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDateTime auctionEnd;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private User seller;

	@OneToMany(mappedBy = "item")
	private Set<Bid> bids = new HashSet<Bid>();

	@ElementCollection
	private Set<String> images = new HashSet<String>();

	public Item() {
	}

	public Item(String name, LocalDateTime auctionEnd, User seller) {
		this.name = name;
		this.auctionEnd = auctionEnd;
		this.seller = seller;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getAuctionEnd() {
		return auctionEnd;
	}

	public void setAuctionEnd(LocalDateTime auctionEnd) {
		this.auctionEnd = auctionEnd;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Set<String> getImages() {
		return images;
	}

	public void setImages(Set<String> images) {
		this.images = images;
	}

}
