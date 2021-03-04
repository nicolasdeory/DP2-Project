/*
 * ExtraWaitConditions.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.testing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public abstract class WaitConditions {

	// Constructors -------------------------------------------------------

	private WaitConditions() {
	}

	// Business methods -----------------------------------------------------

	public static ExpectedCondition<Boolean> safeStalenessOf(final WebElement element, final By locator) {
		assert element instanceof RemoteWebElement;
		assert locator != null;
		
		ExpectedCondition<Boolean> result;
		
		result = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver driver) {
				boolean result;
				WebElement target;
				
				target = driver.findElement(locator);
				assert target instanceof RemoteWebElement;
				result = !((RemoteWebElement)element).getId().equals(((RemoteWebElement)target).getId());
				
				return result;
			}

			@Override
			public String toString() {
				return String.format("staleness of element \"%s\"", ((RemoteWebElement)element).getId());
			}
		};
		
		return result;
	}

}
