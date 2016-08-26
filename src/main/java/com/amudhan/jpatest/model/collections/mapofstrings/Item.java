package com.amudhan.jpatest.model.collections.mapofstrings;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;
/* With the help of MapKeyColumn, Map data structure can be used.
 * The key field is mapped to the column specified using MapKeyColumn.
 * The value field is specified with just Column.*/
@Entity(name="COLLECTIONS_MAPOFSTRINGS_ITEM")
@Table(name="COLLECTIONS_MAPOFSTRINGS_ITEM")
public class Item extends AbstractItem {
	
	@ElementCollection
	@CollectionTable(name="COLLECTIONS_MAPOFSTRINGS_IMAGE")
	@MapKeyColumn(name="FILE_NAME")
	@Column(name="IMAGE_NAME")
	protected Map<String, String> images = new HashMap<String, String>();

	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}
	
}
