package com.amudhan.jpatest.model.associations.onetomany.orphanremoval;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.amudhan.jpatest.AbstractItemTest;

public class ItemTest extends AbstractItemTest {

	@Test(priority = 1)
	@Transactional
	@Commit
	public void orphanRemovalItemInsert(){
		
	}
}
