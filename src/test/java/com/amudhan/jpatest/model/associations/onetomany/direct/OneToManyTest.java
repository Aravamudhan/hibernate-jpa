package com.amudhan.jpatest.model.associations.onetomany.direct;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class OneToManyTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void oneToManyUserInsert() {
		User user = new User();
		user.setName("An awesome user name");
		entityManager.persist(user);
		Item item = new Item();
		item.setItemName("An item name");
		/* This adds the id of the user, as a foreign key to the user table,
		 * from the item table.*/
		item.setBuyer(user);
		entityManager.persist(item);
		/* This creates another table by combining the names of the User and 
		 * Item entities. This new table maintains the relationship from the user side.
		 * The foreign key column in the item table, maintains the relationship ship from
		 * the item side. Unless items are explicitly added to the user, there won't be
		 * an entry in the ITEM_USER join table. Subsequently, the user#items will be empty,
		 * even if each item might have item#buyer reference. */
		/* Using the default options creates two different reference points, one as a 
		 * foreign key in the item table, another a new join table.*/
		user.getBoughtItems().add(item);
		persistedId = user.getId();
	}
	
	@Test(priority = 2)
	@Transactional
	public void oneToManyDisplay(){
		User user = entityManager.find(User.class, persistedId);
		logger.info(user.toString());
		for(Item item : user.getBoughtItems()){
			logger.info(item.toString());
		}
	}
}
