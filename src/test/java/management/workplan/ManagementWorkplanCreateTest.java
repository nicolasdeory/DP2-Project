package management.workplan;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import java.util.List;

class ManagementWorkplanCreateTest extends AcmeTest {

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

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/managament/workplan/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void createPositive(final int recordIndex, String title, String description, String isPublic, String startDate, String finishDate,String tasks) {
        super.signIn("rosa21", "1234");

        super.clickOnMenu("Management", "Create new Workplan");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        if(isPublic.equals("true"))super.clickAndGo(By.id("isPublic$proxy"));
        String s[]=tasks.split(";");
        for(String task:s){
            super.clickAndGo(By.id(task));
        }

        super.fill(By.id("startDateTime"), startDate);
        super.fill(By.id("finishDateTime"), finishDate);
        super.clickOnSubmitButton("Create");


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

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/managament/workplan/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void createNegative(final int recordIndex, String title, String description, String isPublic, String startDate, String finishDate,String tasks) {
        super.signIn("rosa21", "1234");

        super.clickOnMenu("Management", "Create new Workplan");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        if(isPublic.equals("true"))super.clickAndGo(By.id("isPublic$proxy"));
        String s[]=tasks.split(";");
        for(String task:s){
            super.clickAndGo(By.id(task));
        }
        super.fill(By.id("startDateTime"), startDate);
        super.fill(By.id("finishDateTime"), finishDate);
        super.clickOnSubmitButton("Create");
        super.checkErrorsExist();

        super.signOut();
    }


}
