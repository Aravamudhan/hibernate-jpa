package com.amudhan.jpatest.model.fetching.interception;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity(name = "FETCHING_INTERCEPTION_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private LocalDateTime auctionEnd;
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY) 
    @org.hibernate.annotations.LazyToOne( 
       org.hibernate.annotations.LazyToOneOption.NO_PROXY
    )
    protected User seller;

    @NotNull
    @Length(min = 0, max = 4000)
    @Basic(fetch = FetchType.LAZY)
    protected String description;

    public Item() {
    }
    
    public Item(String name, LocalDateTime auctionEnd, User seller, String description){
    	this.name = name;
    	this.auctionEnd = auctionEnd;
    	this.seller = seller;
    	this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
