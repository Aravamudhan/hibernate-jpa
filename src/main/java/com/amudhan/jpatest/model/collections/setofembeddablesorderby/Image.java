package com.amudhan.jpatest.model.collections.setofembeddablesorderby;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Image {

	@org.hibernate.annotations.Parent
	protected Item item;

	@Column(nullable = false)
	protected String title;

	@Column(nullable = false)
	protected String fileName;

	//Unless explicitly mentioned, the properties of an entity are automatically to columns.
	protected int width;

	protected int height;

	public Image() {
	}

	public Image(String title, String fileName, int width, int height) {
		this.title = title;
		this.fileName = fileName;
		this.width = width;
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + height;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
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
		return "Image [title=" + title + ", fileName=" + fileName + ", width="
				+ width + ", height=" + height + "]";
	}

}
