package com.amudhan.jpatest.model.associations.onetoone.jointable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "ASSOCIATIONS_ONETOONE_JOINTABLE_SHIPMENT")
@Table(name = "ASSOCIATIONS_ONETOONE_JOINTABLE_SHIPMENT")
public class Shipment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/* The default is eager. That will trigger a left outer join with the join table,
	 * and an inner join with the inverse join table(user table here).*/
	@OneToOne(fetch = FetchType.LAZY)
	/* JoinTable is for specifying an intermediate table.
	 * When the one-to-one association is optional, JoinTable method
	 * can be used.
	 * A new table is created in this case, if hbm2ddl is set to create.*/
	@JoinTable(
			name = "ONETOONE_JOINTABLE_ITEMSHIPMENT",//Required.
			/* Specifies the foreign key columns to the join table. 
			 * The id of the Shipment table is set as the primary key of the ITEM_SHIPMENT join table.*/
			joinColumns = @JoinColumn (
					name = "SHIPMENT_ID" //This defaults to ID
					),
			/* This is also a foreign key column, but using the entity which
			 * does not own the association. Using an ITEM_ID value, its shipment information can be queried.*/
			inverseJoinColumns = @JoinColumn(
					/* Defaults to AUCTIONEDITEM_ID if the name is not over ridden.*/
                          name = "ITEM_ID", 
			              nullable = false,
			              unique = true
			              )
			)
	private Item auctionedItem;
	
	public Shipment(){}
	
	public Shipment (Item auctionedItem){
		this.auctionedItem = auctionedItem;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getAuctionedItem() {
		return auctionedItem;
	}

	public void setAuctionedItem(Item auctionedItem) {
		this.auctionedItem = auctionedItem;
	}
}
