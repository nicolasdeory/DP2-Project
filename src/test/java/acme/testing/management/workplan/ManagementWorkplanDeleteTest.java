package acme.testing.management.workplan;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

class ManagementWorkplanDeleteTest extends AcmeTest {

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

    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void deletePositive(final int recordIndex, String title, String description, String isPublic,
            String startDate, String finishDate) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");

        super.checkColumnHasValue(1, 0, title);
        super.checkColumnHasValue(1, 2, startDate);
        super.checkColumnHasValue(1, 3, finishDate);

        super.clickOnListingRecord(1);
        String path=super.getSimplePath();
        String query=super.getContextQuery();
        super.checkButtonExists("Delete");
        super.clickOnReturnButton("Delete");

        super.checkColumnDoesNotHaveValue(1, 0, title);

        super.navigate(path,query);
        super.checkErrorsExist();

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void deleteNegative(final int recordIndex, String title, String description, String isPublic,
            String startDate, String finishDate) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 2, startDate);
        super.checkColumnHasValue(recordIndex, 3, finishDate);

        super.clickOnListingRecord(recordIndex);
        super.checkButtonDoesNotExist("Delete");
        super.clickOnReturnButton("Return");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 2, startDate);
        super.checkColumnHasValue(recordIndex, 3, finishDate);

        super.signOut();
    }
}
