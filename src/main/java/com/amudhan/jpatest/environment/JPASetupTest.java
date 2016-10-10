package com.amudhan.jpatest.environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class JPASetupTest extends TransactionManagerTest {

	public String persistenceUnitName;
	public String[] hbmResources;
	public JPASetup jpaSetup;

	@BeforeClass
	public void beforeClass() throws Exception {
		configurePersistenceUnit();
	}

	/*
	 * This should be overridden where ever JPASetupTest is used with the
	 * appropriate persistence unit name.
	 */
	public void configurePersistenceUnit() throws Exception {
		configurePersistenceUnit(null);
	}

	public void configurePersistenceUnit(String persistenceUnitName,
			String... hbmResources) throws Exception {
		this.persistenceUnitName = persistenceUnitName;
		this.hbmResources = hbmResources;
	}

	@BeforeMethod
	public void beforeMethod() throws Exception {
		jpaSetup = new JPASetup(TRANSACTION_MANAGER.databaseProduct,
				persistenceUnitName, hbmResources);
		// Always drop the schema, cleaning up at least some of the artifacts
		// that might be left over from the last run, if it didn't cleanup
		// properly
		jpaSetup.dropSchema();

		jpaSetup.createSchema();
		afterJPABootstrap();
	}

	public void afterJPABootstrap() throws Exception {
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {
		if (jpaSetup != null) {
			beforeJPAClose();
			if (!"true".equals(System.getProperty("keepSchema"))) {
				jpaSetup.dropSchema();
			}
			jpaSetup.getEntityManagerFactory().close();
		}
	}

	public void beforeJPAClose() throws Exception {

	}

	protected long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[4096];
		long count = 0;
		int n;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	protected String getTextResourceAsString(String resource)
			throws IOException {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(resource);
		if (is == null) {
			throw new IllegalArgumentException("Resource not found: "
					+ resource);
		}
		StringWriter sw = new StringWriter();
		copy(new InputStreamReader(is), sw);
		return sw.toString();
	}

	protected Throwable unwrapRootCause(Throwable throwable) {
		return unwrapCauseOfType(throwable, null);
	}

	protected Throwable unwrapCauseOfType(Throwable throwable,
			Class<? extends Throwable> type) {
		for (Throwable current = throwable; current != null; current = current
				.getCause()) {
			if (type != null && type.isAssignableFrom(current.getClass()))
				return current;
			throwable = current;
		}
		return throwable;
	}
}
