package management.workplan;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

public class ManagementWorkPlanListAndShowTest extends AcmeTest {
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
    @CsvFileSource(resources = "/management/workplan/listAndShow.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void listAndShowPositive(final int recordIndex, String title, String description, String isPublic, String startDate, String finishDate,String tasks) {
        super.signIn("juan21", "1234");

        super.clickOnMenu("Management", "My Workplans List");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 2, startDate);
        super.checkColumnHasValue(recordIndex, 3, finishDate);

        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("isPublic", isPublic);
        super.checkInputBoxHasValue("startDateTime", startDate);
        super.checkInputBoxHasValue("finishDateTime", finishDate);
        if(tasks!=null){
            String s[]=tasks.split(";");
            for(String task:s){
                assert super.driver.findElement(By.id(task)).isSelected();
            }
        }


        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/listAndShow.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void listAndShowNegative(final int recordIndex, String title, String description, String isPublic, String startDate, String finishDate,String tasks) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");

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
