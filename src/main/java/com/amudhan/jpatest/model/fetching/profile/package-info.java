@org.hibernate.annotations.FetchProfiles({
    @FetchProfile(name = Item.PROFILE_JOIN_SELLER,
       /* Each override in a profile, names one entity association or collection.*/
        fetchOverrides = @FetchProfile.FetchOverride(
            entity = Item.class,
            association = "seller",
            mode = FetchMode.JOIN
        )),

    @FetchProfile(name = Item.PROFILE_JOIN_BIDS,
        fetchOverrides = @FetchProfile.FetchOverride(
            entity = Item.class,
            association = "bids",
            mode = FetchMode.JOIN
        ))
})

package com.amudhan.jpatest.model.fetching.profile;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchMode;