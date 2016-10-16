package com.amudhan.jpatest.model.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;

import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.JPASetupTest;
import com.amudhan.jpatest.model.concurrency.versiontimestamp.Item;

public class VersioningTimeStamp extends JPASetupTest{

	@Override
	public void configurePersistenceUnit()throws Exception{
		configurePersistenceUnit("ConcurrencyVersioningTimestampPU");
	}

	@Test(expectedExceptions = OptimisticLockException.class)
    public void firstCommitWins() throws Throwable {
        UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpaSetup.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            final Long ITEM_ID = someItem.getId();

            // Load an item and change its name
            tx.begin();
            em = jpaSetup.createEntityManager();
            Item item = em.find(Item.class, ITEM_ID);
            item.setName("New Name");

            // The concurrent second unit of work doing the same
            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
                    try {
                        tx.begin();
                        EntityManager em = jpaSetup.createEntityManager();
                        Item item = em.find(Item.class, ITEM_ID);
                        item.setName("Other Name");
                        /* This updates the item using the id and the value of the lastUpdated column that
                         * was present at the start of this transaction. The update query also updates the
                         * lastUpdated value.*/
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
            	/* This would throw an exception. The value of the lastUpdated column
            	 * is different than that was when this transaction was started.*/
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
