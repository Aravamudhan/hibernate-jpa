package com.amudhan.jpatest.model.associations.onetoone.direct;

import java.math.BigDecimal;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToOneTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToOneJoinTableUserInsert(){
		Bid bid = new Bid();
		bid.setAmount(new BigDecimal(1000));
		entityManager.persist(bid);
		User user = new User();
		user.setName("A great user");
		user.setBid(bid);
		entityManager.persist(user);
		persistedId = user.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToOneJoinTableUserDisplay(){
		/* This fetches bid to with a left join.*/
		User user = entityManager.find(User.class, persistedId);
		logger.info(user.getId()+" "+user.getName());
		logger.info(user.getBid().getId()+" "+user.getBid().getAmount());
	}
}

