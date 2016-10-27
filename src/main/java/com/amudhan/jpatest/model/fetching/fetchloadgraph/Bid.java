package com.amudhan.jpatest.model.fetching.fetchloadgraph;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.validation.constraints.NotNull;

@NamedEntityGraphs({
	@NamedEntityGraph(
				name = "BidBidderItem",
				attributeNodes = {
						@NamedAttributeNode(value = "bidder"),
						@NamedAttributeNode(value = "item")
				}
			),
	@NamedEntityGraph(
				name = "BidBidderItemSellerBids",
				attributeNodes = {
						@NamedAttributeNode(value = "bidder"),
						@NamedAttributeNode(
								value = "item",
								subgraph = "ItemSellerBids"
						)
				},
				subgraphs = {
						@NamedSubgraph(
									name = "ItemSellerBids",
									attributeNodes = {
											@NamedAttributeNode("seller"),
											@NamedAttributeNode("bids")
									}
								)
				}
			)
})
@Entity(name = "FETCHING_FETCHLOADGRAPH_BID")
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private User bidder;
	
	@NotNull
	private BigDecimal amount;

	public Bid() {
	}

	public Bid(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Bid(Item item, User bidder, BigDecimal amount) {
		this.amount = amount;
		this.bidder = bidder;
		this.item = item;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	public Long getId() {
		return id;
	}

}
