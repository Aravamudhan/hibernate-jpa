package com.amudhan.jpatest.model.filtering;

import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.filtering.dynamic.Category;
import com.amudhan.jpatest.model.filtering.dynamic.Item;
import com.amudhan.jpatest.model.filtering.dynamic.User;
import com.amudhan.jpatest.shared.util.TestData;

public class DynamicFilter extends JPASetupTest{

	private Logger logger = LoggerFactory.getLogger(DynamicFilter.class);
	
	@Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringDynamicPU");
    }

    class DynamicFilterTestData {
        TestData categories;
        TestData items;
        TestData users;
    }
    
    public DynamicFilterTestData storeTestData() throws Exception {
    	logger.info("*****Store data starts*****");
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        tx.begin();
        EntityManager em = jpaSetup.createEntityManager();

        DynamicFilterTestData testData = new DynamicFilterTestData();

        testData.users = new TestData(new Long[2]);
        User johndoe = new User("johndoe");
        em.persist(johndoe);
        testData.users.identifiers[0] = johndoe.getId();
        User janeroe = new User("janeroe", 100);
        em.persist(janeroe);
        testData.users.identifiers[1] = janeroe.getId();

        testData.categories = new TestData(new Long[2]);
        Category categoryOne = new Category("One");
        em.persist(categoryOne);
        testData.categories.identifiers[0] = categoryOne.getId();
        Category categoryTwo = new Category("Two");
        em.persist(categoryTwo);
        testData.categories.identifiers[1] = categoryTwo.getId();

        testData.items = new TestData(new Long[3]);
        Item itemFoo = new Item("Foo", categoryOne, johndoe);
        em.persist(itemFoo);
        testData.items.identifiers[0] = itemFoo.getId();
        Item itemBar = new Item("Bar", categoryOne, janeroe);
        em.persist(itemBar);
        testData.items.identifiers[1] = itemBar.getId();
        Item itemBaz = new Item("Baz", categoryTwo, janeroe);
        em.persist(itemBaz);
        testData.items.identifiers[2] = itemBaz.getId();

        tx.commit();
        em.close();
        logger.info("*****Store data ends*****");
        return testData;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void filterItems() throws Throwable{
    	storeTestData();
    	UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
    	try{
    		tx.begin();
    		EntityManager em = jpaSetup.createEntityManager();
    		{

                org.hibernate.Filter filter = em.unwrap(Session.class)
                    .enableFilter("limitByUserRank");

                filter.setParameter("currentUserRank", 0);

                {
                	logger.info("------Retrieving Items----");
					List<Item> items = em.createQuery("select i from FILTERING_DYNAMIC_ITEM i").getResultList();
                    // select * from ITEM where 0 >=
                    //  (select u.RANK from USERS u  where u.ID = SELLER_ID)
                    assertEquals(items.size(), 1);
                }
                em.clear();
                {
                	logger.info("------Criteria queries----");
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    @SuppressWarnings("rawtypes")
					CriteriaQuery criteria = cb.createQuery();
                    criteria.select(criteria.from(Item.class));
                    List<Item> items = em.createQuery(criteria).getResultList();
                    // select * from ITEM where 0 >=
                    //  (select u.RANK from USERS u  where u.ID = SELLER_ID)
                    assertEquals(items.size(), 1);
                }
                em.clear();

                filter.setParameter("currentUserRank", 100);
                logger.info("------Item for the current user with the rank 100----");
                List<Item >items =
                    em.createQuery("select i from FILTERING_DYNAMIC_ITEM i")
                        .getResultList();
                assertEquals(items.size(), 3);
            }
    		em.clear();
    		tx.commit();
    		em.close();
    	}finally{
    		TRANSACTION_MANAGER.rollback();
    	}
    	
    }
}
