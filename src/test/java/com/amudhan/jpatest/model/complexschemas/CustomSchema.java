package com.amudhan.jpatest.model.complexschemas;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;
import com.amudhan.jpatest.model.complexschemas.custom.Bid;
import com.amudhan.jpatest.model.complexschemas.custom.Item;
import com.amudhan.jpatest.model.complexschemas.custom.User;

public class CustomSchema extends TransactionManagerTest {

	private JPASetup jpa;
	private Logger logger = LoggerFactory.getLogger(CustomSchema.class);

	@BeforeClass
	public void beforeMethod() {
		jpa = new JPASetup(DatabaseProduct.H2, "CustomSchemaPU");
		jpa.dropSchema();
		jpa.createSchema();
	}

	/*
	 * From an exception stack trace(throwable variable), this method returns
	 * the matching exception of the given type(exceptionType variable).
	 */
	private Throwable unwrapCauseOfType(Throwable throwable, Class<? extends Throwable> exceptionType) {
		/*
		 * Every exception has a cause. The bottom most exception will have null
		 * for a cause. This listing will return a cause that matches the
		 * argument exceptionType.
		 */
		for (Throwable current = throwable; current != null; current = current.getCause()) {
			if (exceptionType != null && exceptionType.isAssignableFrom(current.getClass()))
				return current;
			throwable = current;
		}
		return throwable;
	}

	@Test
	public void storeLoadDomainValid() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User();
			user.setEmail("test@valid.address");
			user.setUserName("someuser");
			entityManager.persist(user);
			entityManager.flush();
			logger.info("The persisted value : " + entityManager.find(User.class, user.getId()).toString());
		} catch (Exception e) {
			logger.info("Exceptin occured during store and load of user " + e);
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	/*
	 * The email of the user object is set to an invalid value according to the
	 * domain type created in the CreateScript.sql.txt. This test checks whether
	 * that throws an exception, which in turn proves that the user created SQL
	 * domain type is working correctly.
	 */
	@Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
	public void emailDomainInValid() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User();
			// This address is invalid according to the EMAIL_ADDRESS domain
			user.setEmail("@valid.address");
			user.setUserName("someuser");
			entityManager.persist(user);
			entityManager.flush();
			logger.info("The persisted value : " + entityManager.find(User.class, user.getId()).toString());
		} catch (Exception e) {
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
	public void checkColumnInvalid() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User();
			user.setEmail("test@mail.com");
			// Invalid user name as it starts with the word 'admin'.
			user.setUserName("adminUser");
			entityManager.persist(user);
			entityManager.flush();
		} catch (Exception e) {
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		} finally {
			TRANSACTION_MANAGER.rollback();
		}
	}

	@Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
	public void checkSingleRowInvalid() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			// AUCTIONSTART should be before AUCTIONEND. Here that is reversed.
			// It will throw an exception.
			Item item = new Item("A great item", LocalDateTime.now(), LocalDateTime.now().minusDays(2));
			entityManager.persist(item);
			entityManager.flush();
		} catch (Exception e) {
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		} finally {
			tx.rollback();
		}
	}

	@Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
	public void uniqueMultiColumnInvalid() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			// The userName, email combination should be unique.
			// The following two lines will throw an exception
			User userOne = new User("UserNameOne", "test@mail.com");
			User userTwo = new User("UserNameTwo", "test@mail.com");
			entityManager.persist(userOne);
			entityManager.persist(userTwo);
			entityManager.flush();
		} catch (Exception e) {
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		} finally {
			tx.rollback();
		}
	}

	@Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
	public void checkSubSelectConstraint() throws Throwable {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			Item item = new Item("A great item", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
			Bid bid = new Bid(new BigDecimal(10), item);
			//This will fail since the CREATEDON time of the bid is after the AUCTIONEND.
			bid.setCreatedOn(LocalDateTime.now().plusDays(2));
			entityManager.persist(item);
			entityManager.persist(bid);
			entityManager.flush();
		} catch (Exception e) {
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		} finally {
			tx.rollback();
		}
	}

	@Test
	public void storeAndLoad() throws Exception {
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try {
			tx.begin();
			EntityManager em = jpa.createEntityManager();

			User user = new User();
			user.setEmail("valid@test.com");
			user.setUserName("someuser");
			em.persist(user);

			user = new User();
			user.setEmail("valid2@test.com");
			user.setUserName("otheruser");
			em.persist(user);

			tx.commit();
			em.close();

			tx.begin();
			em = jpa.createEntityManager();
			user = em.find(User.class, user.getId());
			assertEquals(user.getUserName(), "otheruser");
			tx.commit();
			em.close();

		} catch (Exception e) {
			logger.info("Exception in storeAndLoad " + e);
		}
	}
}
