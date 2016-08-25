package com.amudhan.jpatest.model.collections.listofstrings;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;
/*Images are mapped using List.
 * When an image value is deleted, hibernate issues one delete and few update queries.
 * These two queries are fired to update the positions of other elements.
 * When an item is deleted in a list, two items before and after need to be updated.*/
@Entity(name="COLLECTIONS_LISTOFSTRINGS_ITEM")
@Table(name="COLLECTIONS_LISTOFSTRINGS_ITEM")
public class Item extends AbstractItem{

	@ElementCollection
	@CollectionTable(name="COLLECTIONS_LISTOFSTRING_IMAGE")
	@OrderColumn//Stores an index in the persistent list starting at 0.
	@Column(name="FILE_NAME")
	private List<String> images = new ArrayList<String>();
	
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	
}
