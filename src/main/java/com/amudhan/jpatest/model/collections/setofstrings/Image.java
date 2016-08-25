package com.amudhan.jpatest.model.collections.setofstrings;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Image {
	
	@Column(name="FILE_NAME", nullable=false)
	@NotNull
	private String fileName;

	public Image(){}
	public Image(String fileName){
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
