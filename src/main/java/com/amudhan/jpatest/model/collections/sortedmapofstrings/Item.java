package com.amudhan.jpatest.model.collections.sortedmapofstrings;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;
/* SortedMap provides ordering on its keys.
 * Ordering or sorting strategy must be provided.*/
@Entity(name = "COLLECTIONS_SORTEDMAPOFSTRINGS_ITEM")
@Table(name = "COLLECTIONS_SORTEDMAPOFSTRINGS_ITEM")
public class Item extends AbstractItem {
	
	@ElementCollection
	@CollectionTable(name="COLLECTIONS_SORTEDMAPOFSTRINGS_IMAGE")
	@MapKeyColumn(name="FILE_NAME")
	@Column(name="IMAGE_NAME")
	@org.hibernate.annotations.SortNatural
	protected SortedMap<String, String> images = new TreeMap<String, String>();

	public SortedMap<String, String> getImages() {
		return images;
	}

	public void setImages(SortedMap<String, String> images) {
		this.images = images;
	} 
}
