/*
 * PerformanceFileHelper.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.testing;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import acme.framework.helpers.PerformanceFileHelper;

public class PerformanceExtension implements BeforeAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {
	
	// Extension interface ----------------------------------------------------

	@Override
	public void beforeAll(final ExtensionContext context) throws Exception {
		PerformanceFileHelper.initialise();			
	}

	@Override
	public void beforeTestExecution(final ExtensionContext context) throws Exception {
		assert context != null;

		Store store;

		store = this.getStore(context);
		store.put("start-time", System.currentTimeMillis());		
	}

	@Override
	public void afterTestExecution(final ExtensionContext context) throws Exception {
		assert context != null;

		Store store;
		long startTime, currentTime, duration;
		
		currentTime = System.currentTimeMillis();
		store = this.getStore(context);
		startTime = store.get("start-time", long.class);
		store.remove("start-time", long.class);
		duration = currentTime - startTime;

		PerformanceFileHelper.writeTestRecord(context, duration);
	}

	// Ancillary methods ------------------------------------------------------

	protected Store getStore(final ExtensionContext context) {
		assert context != null;

		Store result;
		Class<?> clazz;
		Method method;
		Namespace namespace;

		clazz = this.getClass();
		method = context.getRequiredTestMethod();
		namespace = Namespace.create(clazz, method);

		result = context.getStore(namespace);

		return result;
	}

}
