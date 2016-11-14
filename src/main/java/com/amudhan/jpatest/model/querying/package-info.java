@org.hibernate.annotations.NamedQueries({
    @org.hibernate.annotations.NamedQuery(
        name = "findItemsOrderByName",
        query = "select i from QUERYING_ITEM i order by i.name asc"
    )
    ,
    @org.hibernate.annotations.NamedQuery(
        name = "findItemBuyNowPriceGreaterThan",
        query = "select i from QUERYING_ITEM i where i.buyNowPrice > :price",
        timeout = 60, // seconds
        comment = "Custom SQL comment"
    )
})
package com.amudhan.jpatest.model.querying;