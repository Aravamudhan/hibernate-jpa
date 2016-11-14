package com.amudhan.jpatest.model.querying;

import java.util.Date;

public class ItemSummary {

	private Long itemId;

    private String name;

    private Date auctionEnd;

    public ItemSummary() {
    }

    public ItemSummary(Long itemId, String name, Date auctionEnd) {
        this.itemId = itemId;
        this.name = name;
        this.auctionEnd = auctionEnd;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

}
