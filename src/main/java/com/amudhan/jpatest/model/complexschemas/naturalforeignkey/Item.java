package com.amudhan.jpatest.model.complexschemas.naturalforeignkey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "COMPLEXSCHEMAS_NATURALFOREIGNKEY_ITEM")
@Table(name = "COMPLEXSCHEMAS_NATURALFOREIGNKEY_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	/* Only rule for foreign key/join columns are the source column type
	 * and the target column type must be same. The foreign key need not
	 * be the primary key of the target table. It does not even have to be
	 * a column with unique constraint. Although this behavior is not desirable
	 * this is sometimes the case in the legacy databases.*/
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "SELLER_CUSTOMER_NUMBER",
			referencedColumnName = "CUSTOMERNUMBER"
	)
	private User seller;
	
	public Item(){}
	
	public Item(String name){
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
	
}
