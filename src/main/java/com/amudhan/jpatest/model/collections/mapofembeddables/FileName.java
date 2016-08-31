package com.amudhan.jpatest.model.collections.mapofembeddables;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FileName {

	/*fileName, fileExtension must not be null since they are part of the composite key*/
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false)
	private String fileExtension;

	public FileName(){}
	
	public FileName(String fileName, String fileExtension){
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileExtension == null) ? 0 : fileExtension.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
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
		FileName other = (FileName) obj;
		if (fileExtension == null) {
			if (other.fileExtension != null)
				return false;
		} else if (!fileExtension.equals(other.fileExtension))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileName [fileName=" + fileName + ", fileExtension=" + fileExtension + "]";
	}
	
}
