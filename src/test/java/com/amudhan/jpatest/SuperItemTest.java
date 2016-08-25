package com.amudhan.jpatest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

@ContextConfiguration("classpath:configuration/applicationContext-test.xml")
public abstract class SuperItemTest extends AbstractTransactionalTestNGSpringContextTests{
	
	@PersistenceContext
	protected EntityManager entityManager;
	protected static final Logger logger = LoggerFactory.getLogger(SuperItemTest.class);
	
}
