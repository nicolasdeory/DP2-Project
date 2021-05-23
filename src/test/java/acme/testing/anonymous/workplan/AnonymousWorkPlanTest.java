package acme.testing.anonymous.workplan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class AnonymousWorkPlanTest extends AcmeTest{

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
	 @CsvFileSource(resources = "/anonymous/workplan/list.csv", encoding = "utf-8", numLinesToSkip = 1)
	 @Order(10)
	 void listAndShow(final int recordIndex, final String title, final String workload, final String start, final String finish) {
     super.clickOnMenu("Anonymous", "Public Workplans list");

     super.checkColumnHasValue(recordIndex, 0, title);
     super.checkColumnHasValue(recordIndex, 1, workload);
     super.checkColumnHasValue(recordIndex, 2, start);
     super.checkColumnHasValue(recordIndex, 3, finish);
     

     super.clickOnListingRecord(recordIndex);
     super.clickOnReturnButton("Return");
 }
	 
	 
	 @ParameterizedTest
	 @CsvFileSource(resources = "/anonymous/workplan/show.csv", encoding = "utf-8", numLinesToSkip = 1)
	 @Order(10)
	 void ShowDetails(final int recordIndex, final String title, final String description, final String workload, final String start,
		 final String finish, final String tasks) {
		 super.clickOnMenu("Anonymous", "Public Workplans list");
		 super.clickOnListingRecord(recordIndex);

		 super.checkInputBoxHasValue("title", title);
		 super.checkInputBoxHasValue("description", description);
		 super.checkInputBoxHasValue("workload", workload);
		 super.checkInputBoxHasValue("startDateTime", start);
		 super.checkInputBoxHasValue("finishDateTime", finish);
		 if(tasks!=null){
	            final String s[]=tasks.split(";");
	            for(final String task:s){
	                assert super.driver.findElement(By.id(task)).getText().equals(task);
	            }
		 }
		 super.clickOnReturnButton("Return");
	 }
	 
	 
	 @ParameterizedTest
	 @CsvFileSource(resources = "/anonymous/workplan/show.csv", encoding = "utf-8", numLinesToSkip = 1)
	 @Order(3)
	 void listAndShowNegative(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
		 	super.clickOnMenu("Anonymous", "Public Workplans list");
		 	super.clickOnListingRecord(recordIndex);

	        final String path=super.getSimplePath();
	        final String query=super.getContextQuery();

	        super.signIn("lola21","1234");

	        super.navigate(path,query);
	        super.checkErrorsExist();

	        super.signOut();
	    }

}
