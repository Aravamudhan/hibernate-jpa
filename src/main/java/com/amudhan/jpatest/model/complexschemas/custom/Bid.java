package com.amudhan.jpatest.model.complexschemas.custom;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "COMPLEXSCHEMAS_CUSTOM_BID")
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private BigDecimal amount;

	/* It is a best practice to name constraints. Makes for readable logs/error messages.*/
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ITEM_ID"))
	private Item item;

	@NotNull
	private LocalDateTime createdOn;

	public Bid() {
	}

	public Bid(BigDecimal amount, Item item) {
		this.amount = amount;
		this.item = item;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "Bid [id=" + id + ", amount=" + amount + ", createdOn=" + createdOn + "]";
	}
}
