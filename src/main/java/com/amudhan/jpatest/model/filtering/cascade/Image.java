package com.amudhan.jpatest.model.filtering.cascade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "FILTERING_CASCADE_IMAGE")
public class Image {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String imageName;
	
	private String imageUrl;
	
	@ManyToOne(optional = false)
	private Item item;
	
	public Image(){}
	
	public Image(String imageName, String imageUrl, Item item){
		this.imageName = imageName;
		this.imageUrl = imageUrl;
		this.item = item;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Image [imageName=" + imageName + ", imageUrl=" + imageUrl + "]";
	}
	
	
}
