package com.amudhan.jpatest.model.collections.setofembeddables;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "COLLECTIONS_SETOFEMBEDDABLES_ITEM")
@Table(name = "COLLECTIONS_SETOFEMBEDDABLES_ITEM")
public class Item extends AbstractItem {

	@ElementCollection
	@CollectionTable(name = "COLLECTIONS_SETOFEMBEDDABLES_IMAGE")
	@AttributeOverride(
				name = "fileName",
				column = @Column(name = "FILE_NAME", nullable = false)
			)
	protected Set<Image> images = new HashSet<Image>();

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

}
