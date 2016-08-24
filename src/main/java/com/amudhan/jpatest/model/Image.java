package com.amudhan.jpatest.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Image {
	
	@Column(name="title")
	private String title;
	@Column(name="FILE_NAME", nullable=false)
	@NotNull
	private String fileName;

	public Image(){}
	public Image(String fileName, String title){
		this.fileName = fileName;
		this.title = title;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
