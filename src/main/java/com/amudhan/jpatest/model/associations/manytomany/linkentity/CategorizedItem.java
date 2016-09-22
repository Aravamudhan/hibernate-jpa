package com.amudhan.jpatest.model.associations.manytomany.linkentity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "ASSOCIATIONS_MANYTOMANY_LINKENTITY_CATEGORIZEDITEM")
@Table(name = "ASSOCIATIONS_MANYTOMANY_LINKENTITY_CATEGORIZEDITEM")
/* Immutable signifies that the entity, once created, the properties won't be updated.*/
@org.hibernate.annotations.Immutable
public class CategorizedItem {
	
	@EmbeddedId
	private Id id = new Id();
	
	@Column(updatable = false)
	@NotNull
	private String addedBy;
	
	@Column(updatable = false)
	@NotNull
	private LocalDateTime addedOn;

	/* Instead of ManyToMany association between tables, two ManyToOne associations 
	 * to an intervening table is desirable, if there is a chance that the relationship
	 * should contain more information. Here addedBy, addedOn is required. */
	@ManyToOne
	@JoinColumn(
			name = "CATEGORY_ID",
			insertable = false, updatable = false
		)
	private Category category;
	
	@ManyToOne
	@JoinColumn(
			name = "ITEM_ID",
			insertable = false, updatable = false
		)
	private Item item;

	public CategorizedItem() {}
	
	/* The identifier values must be assigned explicitly.*/
	public CategorizedItem(String addedBy, LocalDateTime addedOn, Category category, Item item){
		this.addedBy = addedBy;
		this.addedOn = addedOn;
		this.category = category;
		this.item = item;
		
		this.id.categoryId = category.getId();
		this.id.itemId = item.getId();
		
		category.getCategorizedItems().add(this);
		item.getCategorizedItems().add(this);
	}
	
	public Id getId() {
		return id;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public LocalDateTime getAddedOn() {
		return addedOn;
	}

	public Category getCategory() {
		return category;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "CategorizedItem [id=" + id + "]";
	}

	/* This class can be externalized too.*/
	@SuppressWarnings("serial")
	@Embeddable
	public static class Id implements Serializable{
		
		@Column(name = "CATEGORY_ID")
		private long categoryId;
		
		@Column(name = "ITEM_ID")
		private long itemId;
		
		public Id(){}
		
		public Id(long categoryId, long itemId){
			this.categoryId = categoryId;
			this.itemId = itemId;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (categoryId ^ (categoryId >>> 32));
			result = prime * result + (int) (itemId ^ (itemId >>> 32));
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
			Id other = (Id) obj;
			if (categoryId != other.categoryId)
				return false;
			if (itemId != other.itemId)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Id [categoryId=" + categoryId + ", itemId=" + itemId + "]";
		}

	}
}
