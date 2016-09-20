package com.amudhan.jpatest.model.associations.onetomany.embeddable;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "ASSOCIATIONS_ONETOMANY_EMBEDDABLE_SHIPMENT")
@Table(name = "ASSOCIATIONS_ONETOMANY_EMBEDDABLE_SHIPMENT")
public class Shipment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private LocalDateTime createdOn;
	
	/* This will throw an exception "address references an unknown entity".
	 * The reason is Address is not an entity, but embeddable.*/
	/*@ManyToOne
	Address address;*/

	public Shipment(){}
	
	public Shipment(LocalDateTime createdOn){
		this.createdOn = createdOn;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "Shipment [id=" + id + ", createdOn=" + createdOn + "]";
	}
	
}
