package com.amudhan.jpatest.model.associations.onetomany.list;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.amudhan.jpatest.model.AbstractBid;

@Entity(name="ASSOCIATIONS_ONETOMANY_LIST_BID")
@Table(name="ASSOCIATIONS_ONETOMANY_LIST_BID")
public class Bid extends AbstractBid{
	
    @ManyToOne
	/* Always ManyToOne side is the owning side.
	 * Hence making it only the read only. Otherwise
	 * the collection settings from the OneToMany side would be ignored.
	 * No order column would be inserted, if this is not read only.
	 * When insertion takes place, the settings in the OneToMany side
	 * will come into effect.
	 */
    /* Schema generator ignores OneToMany side, if ManyToOne side is present.*/
	@JoinColumn(
			name = "ITEM_ID",
			updatable = false,
			insertable = false
			)
	@NotNull
	private Item item;
    
	public Bid(){}
	
	public Bid(BigDecimal amount, Item item){
		this.amount = amount;
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Bid [id=" + id + ", amount=" + amount + "]";
	}
	
}
