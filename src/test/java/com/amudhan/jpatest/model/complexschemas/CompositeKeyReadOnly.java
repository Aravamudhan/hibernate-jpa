package com.amudhan.jpatest.model.complexschemas;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.compositekey.readonly.Department;
import com.amudhan.jpatest.model.complexschemas.compositekey.readonly.User;

public class CompositeKeyReadOnly extends TransactionManagerTest {

	private JPASetup jpa;
	private Logger logger = LoggerFactory.getLogger(CompositeKeyReadOnly.class);

	@BeforeClass
	void configurePersistentUnit() {
		jpa = new JPASetup(DatabaseProduct.H2, "CompositeKeyReadOnly");
		jpa.dropSchema();
		jpa.createSchema();
	}

	@Test
	public void compositeKeyReadOnly() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			Department department = new Department("Great department");
			entityManager.persist(department);
			/*
			 * This enforces correct values to be set and conceptually makes
			 * sense compared to the MapsId method.
			 */
			User user = new User("Thunderous user", department, "Seller");
			entityManager.persist(user);
			tx.commit();
			entityManager.close();
			tx.begin();
			EntityManager entityManagerAnother = jpa.createEntityManager();
			User userPersisted = entityManagerAnother.find(User.class,
					user.getId());
			logger.info(userPersisted.toString());
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}
}
