package com.amudhan.jpatest.environment;

import java.util.Locale;

import javax.naming.NamingException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class TransactionManagerTest {

	public static TransactionManagerSetup TRANSACTION_MANAGER;

	@Parameters({ "database", "connectionURL" })
	// @BeforeSuite()
	@BeforeTest()
	public void beforeSuite(@Optional String database,
			@Optional String connectionURL) {
		try {
			TRANSACTION_MANAGER = new TransactionManagerSetup(
					database != null ? DatabaseProduct.valueOf(database
							.toUpperCase(Locale.US)) : DatabaseProduct.H2,
					connectionURL);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	// @AfterSuite(alwaysRun = true)
	@AfterTest(alwaysRun = true)
	public void afterSuite() throws Exception {
		if (TRANSACTION_MANAGER != null)
			TRANSACTION_MANAGER.stop();
	}
}
