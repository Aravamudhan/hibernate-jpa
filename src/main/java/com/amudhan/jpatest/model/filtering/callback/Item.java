package com.amudhan.jpatest.model.filtering.callback;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name="FILTERING_CALLBACK_ITEM")
/*
 * A class to define call back methods for the life cycle events
 * of this entity.
 */
@EntityListeners(
	    PersistEntityListener.class
	)
@ExcludeDefaultListeners
public class Item {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	@NotNull
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELLER_ID", nullable = false)
	private User seller;
	
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

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
	
}

