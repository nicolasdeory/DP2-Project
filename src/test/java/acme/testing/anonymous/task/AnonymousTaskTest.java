package acme.testing.anonymous.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class AnonymousTaskTest extends AcmeTest {

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
        super.sleep(10, true);

    }

    // Test cases -------------------------------------------------------------
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/task/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void listAndShow(final int recordIndex, final String title, final String start, final String finish,
            final String workload, final String link) {
        super.clickOnMenu("Anonymous", "Public Tasks");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, start);
        super.checkColumnHasValue(recordIndex, 2, finish);
        super.checkColumnHasValue(recordIndex, 3, workload);
        super.checkColumnHasValue(recordIndex, 4, link);

        super.clickOnListingRecord(recordIndex);
        super.clickOnReturnButton("Return");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/task/show.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void ShowDetails(final int recordIndex, final String title, final String description, final String start,
            final String finish, final String workload, final String link) {
        super.clickOnMenu("Anonymous", "Public Tasks");
        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("workload", workload);
        super.checkInputBoxHasValue("isPublic", "true");
        super.checkInputBoxHasValue("startDateTime", start);
        super.checkInputBoxHasValue("finishDateTime", finish);
        super.checkInputBoxHasValue("link", link);

        super.clickOnReturnButton("Return");
    }

    // Ancillary methods ------------------------------------------------------
}
