package com.amudhan.jpatest.model.collections.mapofembeddables;

import javax.persistence.Embeddable;

@Embeddable
public class Image {

	private String title;
	private int width;
	private int height;
	
	public Image(){}
	
	public Image(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
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
		if (height != other.height)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Image [title=" + title + ", width=" + width + ", height=" + height + "]";
	}

}
