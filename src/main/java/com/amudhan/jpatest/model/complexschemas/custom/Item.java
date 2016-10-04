package com.amudhan.jpatest.model.complexschemas.custom;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity(name = "COMPLEXSCHEMAS_CUSTOM_ITEM")
@org.hibernate.annotations.Check(constraints = "AUCTIONSTART < AUCTIONEND")
/*
 * This entity is an example for table constraints. Table constraints span
 * multiple columns. UNIQUE is a table constraint. Multiple columns can be
 * assigned the UNIQUE constraint together.
 */
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	@NotNull
	private LocalDateTime auctionStart;
	@NotNull
	private LocalDateTime auctionEnd;
	@OneToMany(mappedBy = "item")
	private Set<Bid> bids = new HashSet<Bid>();

	public Item() {
	}

	public Item(String name, LocalDateTime auctionStart, LocalDateTime auctionEnd) {
		this.name = name;
		this.auctionStart = auctionStart;
		this.auctionEnd = auctionEnd;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getAuctionStart() {
		return auctionStart;
	}

	public void setAuctionStart(LocalDateTime auctionStart) {
		this.auctionStart = auctionStart;
	}

	public LocalDateTime getAuctionEnd() {
		return auctionEnd;
	}

	public void setAuctionEnd(LocalDateTime auctionEnd) {
		this.auctionEnd = auctionEnd;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", auctionStart=" + auctionStart + ", auctionEnd=" + auctionEnd
				+ "]";
	}

}
