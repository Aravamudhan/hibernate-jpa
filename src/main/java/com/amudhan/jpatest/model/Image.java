package com.amudhan.jpatest.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Embeddable
public class Image {
	
	@ManyToOne
	@JoinColumn(name="ITEM_ID", nullable=false)
	private long itemId;
	@Column(name="FILE_NAME", nullable=false)
	@NotNull
	private String fileName;
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
