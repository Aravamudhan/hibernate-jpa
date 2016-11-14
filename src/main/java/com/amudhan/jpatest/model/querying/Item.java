package com.amudhan.jpatest.model.querying;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/*@NamedQueries({
    @NamedQuery(
        name = "findItemById",
        query = "select i from Item i where i.id = :id"
    )
    ,
    @NamedQuery(
        name = "findItemByName",
        query = "select i from Item i where i.name like :name",
        hints = {
            @QueryHint(
                name = org.hibernate.annotations.QueryHints.TIMEOUT_JPA,
                value = "60000"),
            @QueryHint(
                name = org.hibernate.annotations.QueryHints.COMMENT,
                value = "Custom SQL comment")
        }
    )
})
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "ItemResult",
        entities =
        @EntityResult(
            entityClass = Item.class,
            fields = {
                @FieldResult(name = "id", column = "ID"),
                @FieldResult(name = "name", column = "EXTENDED_NAME"),
                @FieldResult(name = "createdOn", column = "CREATEDON"),
                @FieldResult(name = "auctionEnd", column = "AUCTIONEND"),
                @FieldResult(name = "auctionType", column = "AUCTIONTYPE"),
                @FieldResult(name = "approved", column = "APPROVED"),
                @FieldResult(name = "buyNowPrice", column = "BUYNOWPRICE"),
                @FieldResult(name = "seller", column = "SELLER_ID")
            }
        )
    )
})*/
@Entity(name="QUERYING_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
    private String name;

    @NotNull
    private Date createdOn = new Date();

    @NotNull
    private Date auctionEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuctionType auctionType = AuctionType.HIGHEST_BID;

    @NotNull
    private boolean approved = true;

    private BigDecimal buyNowPrice;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ITEM_SELLER_ID"))
    private User seller;

    @ManyToMany(mappedBy = "items")
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "item")
    private Set<Bid> bids = new HashSet<>();

    @ElementCollection
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ITEM_IMAGES_ITEM_ID"))
    private Set<Image> images = new HashSet<>();

    public Item() {
    }

    public Item(String name, Date auctionEnd, User seller) {
        this.name = name;
        this.auctionEnd = auctionEnd;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(AuctionType auctionType) {
        this.auctionType = auctionType;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public BigDecimal getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(BigDecimal buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
