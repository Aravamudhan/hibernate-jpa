package com.amudhan.jpatest.model.collections.setofstrings;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.amudhan.jpatest.model.AbstractItem;

/*Item#images uses Set. Only distinct FILE_NAMEs can be saved for each Item.
 **/
@Entity( name= "COLLECTIONS_SETOFSTRINGS_ITEM")
@Table( name = "COLLECTIONS_SETOFSTRINGS_ITEM")
public class Item extends AbstractItem{
	
	@ElementCollection
	@CollectionTable(name="COLLECTIONS_SETOFSTRINGS_IMAGE", joinColumns=@JoinColumn(name="ITEM_ID"))
	@Column(name="FILE_NAME")
	private Set<String> images = new HashSet<String>();
	
	public Set<String> getImages() {
		return images;
	}
	public void setImages(Set<String> images) {
		this.images = images;
	}

}
