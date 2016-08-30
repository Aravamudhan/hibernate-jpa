package com.amudhan.jpatest.model.collections.setofstringsorderby;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name="COLLECTIONS_SETOFSTRINGSORDERBY_ITEM")
@Table(name="COLLECTIONS_SETOFSTRINGSORDERBY_ITEM")
public class Item extends AbstractItem {
	
	@ElementCollection
	@CollectionTable(name="COLLECTIONS_SETOFSTRINGSORDERBY_IMAGE")
	@Column(name="FILE_NAME")
	/* The clause must contain an SQL statement. The result is not ordered in the memory, 
	   but when it is loaded from the data source.
	*/ 
	@org.hibernate.annotations.OrderBy( clause = "FILE_NAME ASC")
	protected Set<String> images = new LinkedHashSet<String>();

	public Set<String> getImages() {
		return images;
	}

	public void setImages(Set<String> images) {
		this.images = images;
	}
	
}
