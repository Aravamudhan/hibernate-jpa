package com.amudhan.jpatest.model.collections.setofembeddablesorderby;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity( name = "COLLECTIONS_SETOFEMBEDDABLESORDERBY")
@Table( name = "COLLECTIONS_SETOFEMBEDDABLESORDERBY")
public class Item extends AbstractItem {

	@ElementCollection
	@CollectionTable(name = "COLLECTIONS_SETOFEMBEDDABLESORDERBY_IMAGE")
	@OrderBy("FILE_NAME DESC, WIDTH")
	@AttributeOverride(
			name = "fileName",
			column = @Column(name = "FILE_NAME", nullable = false)
	)
	protected Set<Image> images =  new LinkedHashSet<Image>();

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
}
