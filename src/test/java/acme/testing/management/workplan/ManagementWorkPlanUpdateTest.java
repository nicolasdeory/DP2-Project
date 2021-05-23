package acme.testing.management.workplan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

public class ManagementWorkPlanUpdateTest extends AcmeTest{
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
        super.signOut(); 
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");
        super.clickAndGo(By.xpath("//*[@id=\"list\"]/thead/tr/th[2]"));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void updatePositive(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
        super.signIn("juan21", "1234");

        super.clickOnMenu("Management", "My Workplans List");
        super.clickOnListingRecord(recordIndex);
        //Try if the workplan is not finished
        try {
        	super.driver.findElement(By.cssSelector("input[id='title']:not(:disabled)"));
        	
        	super.fillInputBoxIn("title", title);
            super.fillInputBoxIn("description", description);
            if(super.driver.findElement(By.id("isPublic$proxy")).isSelected() == false) {
            	if(isPublic.equals("true"))super.clickAndGo(By.id("isPublic$proxy"));
            } else {
            	if(isPublic.equals("false"))super.clickAndGo(By.id("isPublic$proxy"));
            } 
            if(tasks!=null){
                final String s[]=tasks.split(";");
                for(final String task:s){
                	if (super.driver.findElement(By.id(task)).isSelected() == false) {
                        super.clickAndGo(By.id(task));
                	}
                }
            }


            super.fill(By.id("startDateTime"), startDate);
            super.fill(By.id("finishDateTime"), finishDate);
            super.clickOnSubmitButton("Update");

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
                final String s[]=tasks.split(";");
                for(final String task:s){
                    assert super.driver.findElement(By.id(task)).isSelected();
                }
            }
        } catch (final Exception seleniumException) {
        	//Do nothing
        }
        
        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    public void updateNegative(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
        super.signIn("juan21", "1234");

        super.clickOnMenu("Management", "My Workplans List");

        super.clickOnListingRecord(recordIndex);
        
        try {
        	super.driver.findElement(By.cssSelector("input[id='title']:not(:disabled)"));
        	
        	super.fillInputBoxIn("title", title);
        	super.fillInputBoxIn("description", description);
        	if(isPublic.equals("true"))super.clickAndGo(By.id("isPublic$proxy"));
        	if(tasks!=null){
        		final String s[]=tasks.split(";");
        		for(final String task:s){
        			super.clickAndGo(By.id(task));
        		}
        	}
        	super.fill(By.id("startDateTime"), startDate);
        	super.fill(By.id("finishDateTime"), finishDate);
        	super.clickOnSubmitButton("Update");
        	super.checkErrorsExist();

        	super.signOut();
        } catch (final Exception seleniumException) {
        	//Do nothing
        }
    }
}
