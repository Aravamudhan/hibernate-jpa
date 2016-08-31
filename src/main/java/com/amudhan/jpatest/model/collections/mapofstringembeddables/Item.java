package com.amudhan.jpatest.model.collections.mapofstringembeddables;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

@Entity(name = "COLLECTIONS_MAPOFSTRINGEMBEDDABLES")
@Table(name = "COLLECTIONS_MAPOFSTRINGEMBEDDABLES")
/*Images Map with String key and embeddable value.*/
public class Item extends AbstractItem {

	@ElementCollection
	@CollectionTable(name = "COLLECTIONS_MAPOFSTRINGEMBEDDABLES_IMAGE")
	@MapKeyColumn(name = "FILE_NAME")
	private Map<String, Image> images = new HashMap<String, Image>();

	public Map<String, Image> getImages() {
		return images;
	}
	public void setImages(Map<String, Image> images) {
		this.images = images;
	}
	
}
