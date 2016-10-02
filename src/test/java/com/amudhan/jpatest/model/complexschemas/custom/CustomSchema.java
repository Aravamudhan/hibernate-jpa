package com.amudhan.jpatest.model.complexschemas.custom;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amudhan.jpatest.environment.DatabaseProduct;
import com.amudhan.jpatest.environment.JPASetup;
import com.amudhan.jpatest.environment.TransactionManagerTest;

public class CustomSchema extends TransactionManagerTest{

	private JPASetup jpa;
	private Logger logger = LoggerFactory.getLogger(CustomSchema.class);
	
	@BeforeClass
	public void beforeMethod(){
		jpa = new JPASetup(DatabaseProduct.H2, "CustomSchemaPU");
		jpa.dropSchema();
		jpa.createSchema();
	}
	/*From an exception stack trace(throwable variable), this method returns the matching exception of the given type(exceptionType variable).*/
	private Throwable unwrapCauseOfType(Throwable throwable, Class<? extends Throwable> exceptionType) {
		 /*Every exception has a cause. The bottom most exception will have null for a cause.
		 This listing will return a cause that matches the argument exceptionType.*/
        for (Throwable current = throwable; current != null; current = current.getCause()) {
            if (exceptionType != null && exceptionType.isAssignableFrom(current.getClass()))
                return current;
            throwable = current;
        }
        return throwable;
    }
	
	@Test
	public void storeLoadDomainValid() throws Throwable{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User();
			user.setEmail("test@valid.address");
            user.setUserName("someuser");
            entityManager.persist(user);
           	entityManager.flush();
            logger.info("The persisted value : "+entityManager.find(User.class, user.getId()).toString());
		}catch(Exception e){
			logger.info("Exceptin occured during store and load of user "+e);
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
	/* The email of the user object is set to an invalid value according to the domain type
	 * created in the CreateScript.sql.txt. This test checks whether that throws an exception,
	 * which in turn proves that the user created SQL domain type is working correctly.*/
	@Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
	public void storeLoadDomainInValid() throws Throwable{
		UserTransaction tx = TRANSACTION_MANAGER.getUserTransaction();
		try{
			tx.begin();
			EntityManager entityManager = jpa.createEntityManager();
			User user = new User();
			//This is invalid
			user.setEmail("@valid.address");
            user.setUserName("someuser");
            entityManager.persist(user);
           	entityManager.flush();
            logger.info("The persisted value : "+entityManager.find(User.class, user.getId()).toString());
		}catch(Exception e){
			throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);
		}finally{
			TRANSACTION_MANAGER.rollback();
		}
	}
}
