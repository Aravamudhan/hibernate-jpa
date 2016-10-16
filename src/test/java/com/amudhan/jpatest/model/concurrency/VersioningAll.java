package com.amudhan.jpatest.model.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;

import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.concurrency.versionall.Item;

public class VersioningAll extends JPASetupTest{

	@Override
	public void configurePersistenceUnit()throws Exception{
		configurePersistenceUnit("ConcurrencyVersioningAllPU");
	}
	
	/* This test demonstrates the ability of the Hibernate to
	 * include all the columns in the versioning, instead of just
	 * timestamp or version column.*/
	@Test(expectedExceptions = OptimisticLockException.class)
	public void firstCommitWinds() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpaSetup.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            someItem.setDescription("An awesome item");
            em.persist(someItem);
            tx.commit();
            em.close();
            final Long ITEM_ID = someItem.getId();

            tx.begin();
            em = jpaSetup.createEntityManager();
            Item item = em.find(Item.class, ITEM_ID);
            item.setName("New Name");

            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
                    try {
                        tx.begin();
                        EntityManager em = jpaSetup.createEntityManager();
                        Item item = em.find(Item.class, ITEM_ID);
                        item.setName("Other Name");
                        /* The WHERE clause of the UPDATE contains
                         * DESCRIPTION = 'An awesome item' and NAME = "Some Item AND ID = 1"*/
                        tx.commit();
                        em.close();
                    } catch (Exception ex) {
                        TRANSACTION_MANAGER.rollback();
                        throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                    }
                    return null;
                }
            }).get();

            try {
            	/* The WHERE clause of the generated UPDATE has DESCRIPTION, NAME and ID
            	 * with its last known values "An awesome item", "Some Item" and 1. 
            	 * This will return 0 rows since the name in the row that matches all
            	 * these values is changed to "Other Name". This will throw exception.*/
                tx.commit();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, OptimisticLockException.class);
            }
            em.close();
        } finally {
            TRANSACTION_MANAGER.rollback();
        }
		
	}
}
