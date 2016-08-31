package com.amudhan.jpatest.model.collections.mapofembeddables;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "COLLECTIONS_MAPOFEMBEDDABLES_ITEM")
@Table(name = "COLLECTIONS_MAPOFEMBEDDABLES_ITEM")
/* Images Map with embeddable key and embeddable value.
 * If a Map's key an embeddable, then applying MapKeyColumn has no effect.*/
public class Item extends AbstractItem {

	@ElementCollection
	@CollectionTable(name = "COLLECTIONS_MAPOFEMBEDDABLES_IMAGE")
	private Map<FileName, Image> images = new HashMap<FileName, Image>();

	public Map<FileName, Image> getImages() {
		return images;
	}
	public void setImages(Map<FileName, Image> images) {
		this.images = images;
	}
	
}
