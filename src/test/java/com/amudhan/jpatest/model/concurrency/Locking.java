package com.amudhan.jpatest.model.concurrency;

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PessimisticLockException;
import javax.transaction.UserTransaction;

import org.testng.annotations.Test;

import com.amudhan.jpatest.model.concurrency.version.Category;
import com.amudhan.jpatest.model.concurrency.version.Item;

public class Locking extends Versioning{
	
	@Test
	public void pessimisticReadWrite() throws Exception{
		final ConcurrencyTestData testData = storeCategoriesAndItems();
		Long[] categories = testData.categories.identifiers;
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager em = jpaSetup.createEntityManager();
			BigDecimal totalPrice = new BigDecimal(0);
			for(Long categoryId : categories){
				@SuppressWarnings("unchecked")
				List<Item> items = em.createQuery("SELECT i from CONCURRENCY_VERSION_ITEM i where i.category.id = :catId").
						setLockMode(LockModeType.PESSIMISTIC_READ).
						setParameter("catId", categoryId).
						getResultList();
				for(Item item : items){
					totalPrice = totalPrice.add(item.getBuyNowPrice());
				}
				if(categoryId.equals(testData.categories.getFirstId())){
					Executors.newSingleThreadExecutor().submit(new Callable<Object>(){

						@Override
						public Object call() throws Exception {
							try{
								@SuppressWarnings("unchecked")
								List<Item> items = em.createQuery("SELECT i from CONCURRENCY_VERSION_ITEM i where i.category.id = :catId").
										setParameter("catId", testData.categories.getFirstId()).
										setLockMode(LockModeType.PESSIMISTIC_WRITE).
										getResultList();
								Category lastCategory = em.getReference(
	                                    Category.class, testData.categories.getLastId()
	                                );
								items.iterator().next().setCategory(lastCategory);
								tx.commit();
	                            em.close();
							}catch(Exception ex){
								TRANSACTION_MANAGER.rollback();
//								assertTrue(ex instanceof PessimisticLockException);
							}
							return null;
						}
						
					}).get();
				}
			}
			tx.commit();
			em.close();
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}

}
