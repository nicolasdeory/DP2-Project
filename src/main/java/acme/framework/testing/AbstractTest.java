/*
 * AbstractTest.java
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

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import acme.framework.helpers.PerformanceFileHelper;
import acme.framework.helpers.StringHelper;
import lombok.Getter;
import lombok.Setter;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(PerformanceExtension.class)
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


	protected void setBaseCamp(final String protocol, final String host, final String port, final String contextPath, final String contextHome, final String contextQuery) {
		assert !StringHelper.isBlank(protocol);
		assert !StringHelper.isBlank(host);
		assert !StringHelper.isBlank(port);
		assert !StringHelper.isBlank(contextPath) && contextPath.startsWith("/") && !contextPath.endsWith("/");
		assert !StringHelper.isBlank(contextHome) && contextHome.startsWith("/") && !contextHome.endsWith("/");
		assert !StringHelper.isBlank(contextQuery) && contextQuery.startsWith("?");

		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.contextPath = contextPath;
		this.contextQuery = contextQuery;

		this.baseUrl = String.format("%s://%s:%s%s", protocol, host, port, contextPath);
		this.homeUrl = String.format("%s%s%s", this.baseUrl, contextHome, contextQuery);
	}


	@Getter
	@Setter
	protected boolean				headless;

	@Getter
	@Setter
	protected boolean				autoPausing;

	@Getter
	@Setter
	protected int					defaultTimeout;

	// Internal state ---------------------------------------------------------

	protected static int			MAX_URL_FETCH_ATTEMPTS	= 10;
	protected FirefoxOptions		options;
	protected WebDriver				driver;
	protected JavascriptExecutor	executor;
	protected Random				random;

	// Constructor ------------------------------------------------------------


	protected AbstractTest() {
		super();
		this.headless = false;
		this.autoPausing = false;
		this.defaultTimeout = 30;
	}

	// JUnit interface --------------------------------------------------------

	@BeforeAll
	protected void beforeAll() {
		this.options = new FirefoxOptions();
		this.options.setHeadless(this.headless);
		this.options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		this.options.setAcceptInsecureCerts(true);

		this.driver = new FirefoxDriver(this.options);
		this.driver.manage().timeouts().implicitlyWait(this.defaultTimeout, TimeUnit.SECONDS);
		this.driver.manage().window().maximize();

		this.executor = (JavascriptExecutor) this.driver;

		this.random = new Random();
	}

	@BeforeEach
	protected void beforeEach() {
		this.driver.manage().deleteAllCookies();
		this.navigateHome();
	}

	@AfterEach
	protected void afterEach() {
		;
	}

	@AfterAll
	protected void afterAll() {
		if (this.driver != null)
			this.driver.quit();
	}

	// Sleep methods ----------------------------------------------------------

	protected void sleep(final int duration, final boolean exact) {
		assert duration >= 0 && duration <= 3600;

		long autoPause;

		try {
			if (exact) {
				Thread.sleep(1000L * duration);
			} else if (this.autoPausing) {
				autoPause = 1000L * (1 + this.random.nextInt(duration));
				Thread.sleep(autoPause);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
	}

	protected void shortSleep() {
		this.sleep(2, false);
	}

	protected void longSleep() {
		this.sleep(5, false);
	}

	// Path-related methods ---------------------------------------------------

	protected String getCurrentUrl() {
		String result;
		int counter;
		String currentUrl;

		this.waitUntilComplete();
		currentUrl = this.driver.getCurrentUrl();
		result = this.extractSimplePath(currentUrl);
		for (counter = 0; counter < AbstractTest.MAX_URL_FETCH_ATTEMPTS && result.equals("/master/referrer"); counter++) {
			this.sleep(counter + 1, true);
			currentUrl = this.driver.getCurrentUrl();
			result = this.extractSimplePath(currentUrl);
		}
		assert !result.equals("/master/referrer") : "The '/master/referrer' redirector didn't work";

		return result;
	}

	protected String getSimplePath() {
		String result;
		String currentUrl;

		currentUrl = this.getCurrentUrl();
		result = this.extractSimplePath(currentUrl);

		return result;
	}

	protected void checkSimplePath(final String expectedPath) {
		assert this.isSimplePath(expectedPath);

		String currentUrl, currentPath;

		if (!expectedPath.equals("#")) {
			this.waitUntilComplete();
			currentUrl = this.getCurrentUrl();
			currentPath = this.extractSimplePath(currentUrl);
			assert currentPath.equals(expectedPath) : String.format("The system doesn't navigate from '%s' to '%s'", currentPath, expectedPath);
		}
	}

	// Location methods -------------------------------------------------------

	protected WebElement locateOne(final By locator) {
		assert locator != null;
		assert this.exists(locator) : String.format("Cannot find '%s'", locator.toString());

		WebElement result;

		result = this.driver.findElement(locator);

		return result;
	}

	protected List<WebElement> locateMany(final By locator) {
		assert locator != null;
		assert this.exists(locator) : String.format("Cannot find '%s'", locator.toString());

		List<WebElement> result;

		result = this.driver.findElements(locator);

		return result;
	}

	protected boolean exists(final By locator) {
		assert locator != null;

		boolean result;

		try {
			this.driver.findElement(locator);
			result = true;
		} catch (final Throwable oops) {
			result = false;
		}

		return result;
	}

	protected void checkExists(final By locator) {
		assert locator != null;

		assert this.exists(locator) : String.format("Element '%s' is expected", locator);
	}

	protected void checkNotExists(final By locator) {
		assert locator != null;

		assert !this.exists(locator) : String.format("Element '%s' is not expected", locator);
	}

	// Form-filling methods ---------------------------------------------------

	protected void clear(final By locator) {
		assert locator != null;

		WebElement element;

		element = this.locateOne(locator);
		element.clear();
		this.waitUntilComplete();
		this.shortSleep();
	}

	protected void fill(final By locator, final String value) {
		assert locator != null;
		// value is nullable

		WebElement element;

		element = this.locateOne(locator);
		element.clear();
		if (!StringHelper.isBlank(value))
			element.sendKeys(value);
		this.waitUntilComplete();
		this.shortSleep();
	}

	// Navigation methods -----------------------------------------------------

	protected void navigateHome() {
		this.navigate(() -> this.driver.get(this.homeUrl));
	}

	protected void navigate(final String path, final String query) {
		assert this.isSimplePath(path);
		assert this.isSimpleQuery(query);

		String url;
		
		url = String.format("%s%s%s%s", this.baseUrl, path, this.contextQuery, query);		
		this.navigate(() -> this.driver.get(url));		
	}

	protected void navigate(final Runnable navigator) {
		assert navigator != null;

		By locator;
		WebElement html;
		long startTime, endTime, duration;
		String simplePath;

		locator = By.tagName("html");
		html = this.driver.findElement(locator);
		assert html != null;

		startTime = System.currentTimeMillis();
		navigator.run();
		this.waitStalenessOf(html);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		simplePath = this.getSimplePath();
				 
		PerformanceFileHelper.writeRequestRecord(simplePath, duration);

		this.longSleep();
	}

	// Click methods ----------------------------------------------------------

	protected void clickAndGo(final By locator) {
		assert locator != null;

		WebElement element;

		element = this.locateOne(locator);
		this.clickAndGo(element);
	}

	protected void clickAndGo(final WebElement element) {
		assert element != null;

		// INFO: WebElement::click is a nightmare.  Don't use it!
		this.executor.executeScript("arguments[0].click();", element);		
		this.waitUntilComplete();
		this.shortSleep();
	}

	protected void clickAndWait(final By locator) {
		assert locator != null;

		WebElement element;

		element = this.locateOne(locator);
		this.clickAndWait(element);
	}

	protected void clickAndWait(final WebElement element) {
		assert element != null;

		this.navigate(() -> this.executor.executeScript("arguments[0].click();", element));
	}

	// Ancillary methods ------------------------------------------------------

	protected boolean isSimplePath(final String path) {
		boolean result;

		result = !StringHelper.isBlank(path) && //
			(path.equals("#") || //
				path.startsWith("/") && !path.endsWith("/") && !path.contains("?") //
			);

		return result;
	}

	protected boolean isSimpleQuery(final String query) {
		boolean result;

		result = query == null || (!query.startsWith("?") && !query.startsWith("&"));

		return result;
	}

	protected String extractSimplePath(final String url) {
		assert !StringHelper.isBlank(url);

		String result;
		int queryPosition;

		result = url.replace(this.baseUrl, "");
		queryPosition = result.indexOf("?");
		if (queryPosition != -1)
			result = result.substring(0, queryPosition);

		return result;
	}

	protected void waitStalenessOf(final WebElement html) {
		assert html != null;

		By locator;
		WebDriverWait wait;

		try {
			locator = By.tagName("html");
			wait = new WebDriverWait(this.driver, this.defaultTimeout);
			wait.until(WaitConditions.stalenessOf(html, locator));
		} catch (final Throwable oops) {
			assert false : "Timeout waiting for action to complete";
		}
	}

	protected void waitUntilComplete() {
		WebDriverWait wait;

		try {
			wait = new WebDriverWait(this.driver, this.defaultTimeout);
			wait.until(WaitConditions.documentComplete());
		} catch (final Throwable oops) {
			assert false : "Timeout waiting for action to complete";
		}
	}

}
