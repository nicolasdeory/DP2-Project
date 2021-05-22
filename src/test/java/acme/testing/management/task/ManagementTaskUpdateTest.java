package acme.testing.management.task;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

public class ManagementTaskUpdateTest extends AcmeTest {
    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(true);

        this.signIn("administrator", "administrator");
        super.clickAndGo(By.linkText("Administrator"));
        super.clickAndGo(By.linkText("Populate DB (initial)"));
        super.checkAlertExists(true);
        super.clickAndGo(By.linkText("Administrator"));
        super.clickAndGo(By.linkText("Populate DB (samples)"));
        super.checkAlertExists(true);
        sleep(10,true);

    }
    // Test cases -------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void updatePositive(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                            final String link) {
        super.signIn("rosa21", "1234");
        super.clickOnMenu("Management", "My tasks");



        super.clickOnListingRecord(recordIndex);

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        Boolean selected=super.driver.findElement(By.id("isPublic$proxy")).isSelected();
        Boolean isPublicBool=Boolean.valueOf(isPublic);
        if(!isPublicBool.equals(selected))super.clickAndGo(By.id("isPublic$proxy"));
        super.fillInputBoxIn("workload",workload);
        super.fillInputBoxIn("startDateTime", start);
        super.fillInputBoxIn("finishDateTime", finish);
        super.fillInputBoxIn("link",link);

        super.clickOnSubmitButton("Update");

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
    @CsvFileSource(resources = "/management/task/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void updateNegative(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                               final String link){
        super.signIn("rosa21", "1234");
        super.clickOnMenu("Management", "My tasks");

        super.clickOnListingRecord(recordIndex);

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        Boolean selected=super.driver.findElement(By.id("isPublic$proxy")).isSelected();
        Boolean isPublicBool=Boolean.valueOf(isPublic);
        if(!isPublicBool.equals(selected))super.clickAndGo(By.id("isPublic$proxy"));
        super.fillInputBoxIn("workload",workload);
        super.fillInputBoxIn("startDateTime", start);
        super.fillInputBoxIn("finishDateTime", finish);
        super.fillInputBoxIn("link",link);

        super.clickOnSubmitButton("Update");
        super.checkErrorsExist();

        super.signOut();
    }
}
