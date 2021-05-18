package acme.testing.manager.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.By;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmeTest;

public class ManagerTaskTest extends AcmeTest {

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
        super.clickAndGo(By.linkText("Administrator"));
        super.clickAndGo(By.linkText("Populate DB (samples)"));
        super.checkAlertExists(true);
        super.sleep(10,true);
    }

    // Test cases -------------------------------------------------------------
    @ParameterizedTest
    @CsvFileSource(resources = "/manager/task/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void CreatePositive(final int recordIndex, final String title, final String description,
            final String workload, final String start, final String end, final String link) {

        super.signIn("Lola21", "1234");
        super.clickOnMenu("Management", "Create task");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        super.fillInputBoxIn("workload", workload);
        super.fillInputBoxIn("startDateTime", start);
        super.fillInputBoxIn("finishDateTime", end);
        super.fillInputBoxIn("link", link);

        super.clickOnSubmitButton("Create");

        super.clickOnMenu("Management", "My tasks");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, description);
        super.checkColumnHasValue(recordIndex, 2, start);
        super.checkColumnHasValue(recordIndex, 3, end);
        super.checkColumnHasValue(recordIndex, 4, workload);
        super.checkColumnHasValue(recordIndex, 5, link);

        super.clickOnListingRecord(0);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/manager/task/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void CreateNegative(final int recordIndex, final String title, final String description,
            final String workload, final String start, final String end, final String link) {

        super.signIn("Juan21", "1234");
        super.clickOnMenu("Management", "Create task");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        super.fillInputBoxIn("workload", workload);
        super.fillInputBoxIn("startDateTime", start);
        super.fillInputBoxIn("finishDateTime", end);
        super.fillInputBoxIn("link", link);

        super.clickOnSubmitButton("Create");
        super.checkErrorsExist();
    }
    // Ancillary methods ------------------------------------------------------
}
