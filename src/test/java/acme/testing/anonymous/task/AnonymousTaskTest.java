package acme.testing.anonymous.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmeTest;

public class AnonymousTaskTest extends AcmeTest {

    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(true);

    }

    // Test cases -------------------------------------------------------------
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/task/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void listAndShow(final int recordIndex, final String title, final String start, final String finish,
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
    public void ShowDetails(final int recordIndex, final String title, final String description, final String start,
            final String finish, final String workload, final String link) {
        super.clickOnMenu("Anonymous", "Public Tasks");
        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("Title", title);
        super.checkInputBoxHasValue("Description", description);
        super.checkInputBoxHasValue("Workload", workload);
        super.checkInputBoxHasValue("Start", start);
        super.checkInputBoxHasValue("Finish", finish);
        super.checkInputBoxHasValue("Link", link);

        super.clickOnReturnButton("Return");
    }

    // Ancillary methods ------------------------------------------------------
}
