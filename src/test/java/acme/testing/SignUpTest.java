/*
 * SignUpTest.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.framework.testing.AbstractTest;

class SignUpTest extends AcmeTest {

	// Internal state ---------------------------------------------------------

	// Lifecycle management ---------------------------------------------------

	@Override
	@BeforeAll
	public void beforeAll() {
		super.beforeAll();

		super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
		super.setAutoPausing(false);

		this.signIn("administrator", "administrator");
		super.clickAndGo(By.linkText("Administrator"));
		super.clickAndGo(By.linkText("Populate DB (initial)"));
		super.checkAlertExists(true);
		this.signOut();
	}

	// Test cases -------------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/sign-up/positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	void positiveSignUp(final String username, final String password, final String name, final String surname, final String email) {
		this.signUp(username, password, name, surname, email);
		this.signIn(username, password);
		Assertions.assertTrue(super.exists(By.linkText("Account")), "account doesnt exist");
		this.signOut();
	}

	// Ancillary methods ------------------------------------------------------


}
