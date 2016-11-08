package com.amudhan.jpatest.model.filtering.envers;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

@Entity(name = "FILTERING_ENVERS_Bid")
@Audited
public class Bid {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    private BigDecimal amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Item item;

    public Bid() { }

    public Bid(BigDecimal amount, Item item) {
        this.amount = amount;
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Item getItem() {
        return item;
    }

}
