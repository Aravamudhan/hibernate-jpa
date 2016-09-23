package com.amudhan.jpatest.model.associations.manytomany.ternary;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Embeddable
public class CategorizedItem {
	
	@ManyToOne
	@JoinColumn(
			name = "USER_ID",
			updatable = false
		)
	/* For Embeddable if any column is annotated with nullable = false
	 * that becomes part of the primary key. To avoid that using NotNull.
	 * The downside is that the schema does not contain the not null constraint.
	 * Hibernate ignores bean validation annotations for schema generation.*/
	@NotNull
	private User addedBy;
	
	/* Temporal annotation does not support LocalDateTime API.
	 * Hence changed it to util.Date.*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@NotNull
	private Date addedOn;

	@ManyToOne
	@JoinColumn(
			name = "ITEM_ID",
			insertable = false, updatable = false
		)
	private Item item;

	public CategorizedItem() {}
	
	public CategorizedItem(User addedBy, Item item){
		this.addedBy = addedBy;
		this.item = item;
	}
	
	public User getAddedBy() {
		return addedBy;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "CategorizedItem [addedBy=" + addedBy + ", addedOn=" + addedOn
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addedBy == null) ? 0 : addedBy.hashCode());
		result = prime * result + ((addedOn == null) ? 0 : addedOn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategorizedItem other = (CategorizedItem) obj;
		if (addedBy == null) {
			if (other.addedBy != null)
				return false;
		} else if (!addedBy.equals(other.addedBy))
			return false;
		if (addedOn == null) {
			if (other.addedOn != null)
				return false;
		} else if (!addedOn.equals(other.addedOn))
			return false;
		return true;
	}

}
