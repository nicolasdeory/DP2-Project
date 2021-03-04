/*
 * AbstractTest.java
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.Setter;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public abstract class AbstractTest {

	// Properties -------------------------------------------------------------

	@Getter
	protected String	protocol;
	
	@Getter
	protected String	host;
	
	@Getter
	protected String	port;
	
	@Getter
	protected String	contextPath;
	
	@Getter
	protected String	contextHome;
	
	@Getter
	protected String	contextQuery;

	@Getter
	protected String	baseUrl;
	
	@Getter
	protected String	homeUrl;
	
	public void setBaseCamp(
		final String protocol, final String host, final String port, 
		final String contextPath, final String contextHome, final String contextQuery) {
		
		assert protocol != null;
		assert host != null;
		assert port != null;
		assert contextPath != null && contextPath.startsWith("/") && !contextPath.endsWith("/");
		assert contextHome != null && contextHome.startsWith("/") && !contextHome.endsWith("/");
		assert contextQuery != null && contextQuery.startsWith("?");

		this.baseUrl = String.format("%s://%s:%s%s", protocol, host, port, contextPath);
		this.homeUrl = String.format("%s?%s%s", this.baseUrl, contextHome, contextQuery);
	}
	
	@Getter @Setter
	protected boolean autoPausing;
	
	// Internal state ---------------------------------------------------------

	protected FirefoxOptions	options;
	protected WebDriver			driver;
	protected Random			random;

	// Constructor ------------------------------------------------------------

	protected AbstractTest() {
		super();
	}

	// JUnit interface --------------------------------------------------------

	@BeforeAll
	public void beforeAll() {
		this.options = new FirefoxOptions();
		this.options.setHeadless(false);
		this.options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		this.options.setAcceptInsecureCerts(true);

		this.driver = new FirefoxDriver(this.options);
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		this.random = new Random();		
	}

	@BeforeEach
	public void beforeEach() {
	}

	@AfterEach
	public void afterEach() {
	}

	@AfterAll
	public void afterAll() {
		if (this.driver != null)
			this.driver.quit();
	}

	// Business methods -------------------------------------------------------

	public void sleep(final int maxDuration) {
		assert maxDuration >= 0 && maxDuration <= 3600;
		long duration;

		try {
			if (this.autoPausing) {
				duration = 1000L * this.random.nextInt(maxDuration);
				Thread.sleep(duration);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
	}

	public void shortSleep() {
		this.sleep(2);
	}

	public void longSleep() {
		this.sleep(5);
	}

	public WebElement locate(final By by) {
		assert by != null;

		WebElement result;

		result = this.driver.findElement(by);
		Assertions.assertNotNull(result, "Cannot locate element");

		return result;
	}
	
	public boolean exists(final By by) {
		boolean result;
		
		try {
			this.driver.findElement(by);
			result = true;
		} catch (final Throwable oops) {
			result = false;
		}
		
		return result;		
	}

	public void navigateHome() {
		this.driver.get(this.homeUrl);
		this.longSleep();
	}

	public void navigate(final String path, final String query) {
		assert path != null && path.startsWith("/");
		// assert query is nullable

		String url;

		url = String.format("%s/%s?%s&%s", this.baseUrl, path, query, this.contextQuery);
		this.driver.get(url);
		this.longSleep();
	}

	public void clear(final By locator) {
		assert locator != null;

		WebElement element;

		element = this.locate(locator);
		element.clear();
		this.shortSleep();
	}

	public void click(final By locator) {
		assert locator != null;

		WebElement element;

		element = this.locate(locator);
		element.click();
		this.shortSleep();
	}

	public void submit(final By locator) {
		assert locator != null;

		WebElement oldHtml, element;
		By htmlLocator;
		WebDriverWait wait;

		htmlLocator = By.tagName("html");
		oldHtml = this.driver.findElement(htmlLocator);
		element = this.locate(locator);
		element.click();
		this.longSleep();

		wait = new WebDriverWait(this.driver, 30);
		wait.until(WaitConditions.safeStalenessOf(oldHtml, htmlLocator));
	}

	public void fill(final By locator, final String value) {
		assert locator != null;
		// assert value is nullable

		WebElement element;

		element = this.locate(locator);
		element.clear();
		if (value != null && !value.isEmpty())
			element.sendKeys(value);
		this.shortSleep();
	}

}
