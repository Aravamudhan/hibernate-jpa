package com.amudhan.jpatest.model.collections.bagofembeddables;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "COLLECTIONS_BAGOFEMBEDDABLES_ITEM")
@Table(name = "COLLECTIONS_BAGOFEMBEDDABLES_ITEM")
public class Item extends AbstractItem {

	@ElementCollection
	@CollectionTable(name = "COLLECTIONS_BAGOFEMBEDDABLES_IMAGE")
	@org.hibernate.annotations.CollectionId(
			columns = @Column(name ="IMAGE_ID"),
			type = @org.hibernate.annotations.Type(type = "long"),
			generator = "ID_GENERATOR"
			)
	private Collection<Image> images = new ArrayList<Image>();

	public Collection<Image> getImages() {
		return images;
	}

	public void setImages(Collection<Image> images) {
		this.images = images;
	}
}
