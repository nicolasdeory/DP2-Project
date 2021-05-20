package management.task;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

public class ManagementTaskShowTest extends AcmeTest {
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
    @CsvFileSource(resources = "/management/task/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void listAndShowPositive(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                             final String link) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My tasks");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, description);
        super.checkColumnHasValue(recordIndex, 2, start);
        super.checkColumnHasValue(recordIndex, 3, finish);
        super.checkColumnHasValue(recordIndex, 4, workload);
        super.checkColumnHasValue(recordIndex, 5, link);

        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("isPublic", isPublic);
        super.checkInputBoxHasValue("startDateTime", start);
        super.checkInputBoxHasValue("finishDateTime", finish);
        super.checkInputBoxHasValue("workload",workload);
        super.checkInputBoxHasValue("link",link);

        super.signOut();
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void listAndShowNegative(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                            final String link) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My tasks");

        super.clickOnListingRecord(recordIndex);

        String path=super.getSimplePath();
        String query=super.getContextQuery();

        super.signOut();
        super.signIn("lola21","1234");

        super.navigate(path,query);
        super.checkErrorsExist();

        super.signOut();
    }
}
