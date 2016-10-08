package com.amudhan.jpatest.model.complexschemas.compositekey.manytoone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "COMPLEXSCHEMAS_COMPOSITEKEY_MANYTOONE_ITEM")
@Table(name = "COMPLEXSCHEMAS_COMPOSITEKEY_MANYTOONE_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	/* This is an example of composite foreign keys.*/
	@NotNull
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "SELLER_USERNAME",
					referencedColumnName = "USERNAME"),
			@JoinColumn(name = "SELLER_DEPARTMENT_NUMBER",
					referencedColumnName = "DEPARTMENTNUMBER") 
			})
	private User seller;

	public Item() {
	}

	public Item(String name) {
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

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
}
