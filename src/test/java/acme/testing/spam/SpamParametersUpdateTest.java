package acme.testing.spam;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

class SpamParametersUpdateTest extends AcmeTest {
    // Lifecycle management --------------------------------------------------- 
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(false);
        super.signIn("administrator", "administrator");
        super.clickAndGo(By.linkText("Administrator"));
        super.clickAndGo(By.linkText("Populate DB (initial)"));
        super.checkAlertExists(true);
    }
    // Test cases -------------------------------------------------------------

    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/spam/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void updateThresholdPositive(final int recordIndex, final String threshold, final String expectedThreshold) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("threshold", threshold);
        super.clickOnSubmitButton("Update");

        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkInputBoxHasValue("threshold", expectedThreshold);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/spam/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    void updateThresholdNegative(final int recordIndex, final String threshold) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("threshold", threshold);
        super.clickOnSubmitButton("Update");
        super.checkErrorsExist();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/spam/add-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void addKeywordPositive(final int recordIndex, final String keyword) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("newKeyword", keyword);
        super.clickAndGo(By.id("add-keyword"));
        super.clickOnSubmitButton("Update");

        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkExists(By.cssSelector("option[value=\"" + keyword + "\"]"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/spam/add-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    void addKeywordNegative(final int recordIndex, final String keyword) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("newKeyword", keyword);
        super.clickAndGo(By.id("add-keyword"));
        super.clickOnSubmitButton("Update");

        super.checkErrorsExist();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/spam/remove.csv", encoding = "utf-8", numLinesToSkip = 1)
    void removeKeyword(final int recordIndex, final String keyword) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        Select select = new Select(super.driver.findElement(By.id("keywords")));
        select.selectByValue(keyword);
        super.clickAndGo(By.id("remove-keyword"));
        super.clickOnSubmitButton("Update");
        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkNotExists(By.xpath("//select/option[@value='" + keyword + "']"));
    }
} 
