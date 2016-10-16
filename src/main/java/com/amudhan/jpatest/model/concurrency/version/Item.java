package com.amudhan.jpatest.model.concurrency.version;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity(name="CONCURRENCY_VERSION_ITEM")
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Version
	private long version;
	
	@NotNull
	private String name;
	
	private BigDecimal buyNowPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "IMAGE",
			joinColumns = @JoinColumn(name = "ITEM_ID")
		)
	@Column(name = "IMAGE_FILE_NAME")
	private Set<String> images = new HashSet<String>();
	
	public Item(){}
	
	public Item(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBuyNowPrice() {
		return buyNowPrice;
	}

	public void setBuyNowPrice(BigDecimal buyNowPrice) {
		this.buyNowPrice = buyNowPrice;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<String> getImages() {
		return images;
	}

	public void setImages(Set<String> images) {
		this.images = images;
	}

	public Long getId() {
		return id;
	}

	public long getVersion() {
		return version;
	}

}
