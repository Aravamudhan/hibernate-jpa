package com.amudhan.jpatest.model.collections.bagofstrings;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;
/*Bag data structure is used to map Item#images.
 *A Bag is an unordered collection of items.
 *They are just like Set, except with duplicates and just like List but without index. 
 *Bags incur more performance costs.*/
@Entity(name="COLLECTIONS_BAGOFSTRINGS_ITEM")
@Table(name="COLLECTIONS_BAGOFSTRINGS_ITEM")
public class Item extends AbstractItem{
	
	/*This allows duplicate FILE_NAME*/
	@ElementCollection
	@CollectionTable(name="COLLECTIONS_BAGOFSTRINGS_IMAGE")
	@Column(name="FILE_NAME")
	@org.hibernate.annotations.CollectionId(
				columns = @Column(name = "IMAGE_ID"),
				type = @org.hibernate.annotations.Type(type = "long"),
				generator = "ID_GENERATOR")
	private Collection<String> images = new ArrayList<String>();
	
	public Collection<String> getImages() {
		return images;
	}
	public void setImages(Collection<String> images) {
		this.images = images;
	}
	
}
