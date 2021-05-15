package acme.testing.anonymous.workplan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmeTest;

public class AnonymousWorkPlanTest extends AcmeTest{

	 @Override
	    @BeforeAll
	    public void beforeAll() {
	        super.beforeAll();

	        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
	        super.setAutoPausing(false);
	    }
	 
	 // Test cases -------------------------------------------------------------
	 @ParameterizedTest
	 @CsvFileSource(resources = "/anonymous/workplan/list.csv", encoding = "utf-8", numLinesToSkip = 1)
	 @Order(10)
	 public void listAndShow(final int recordIndex, final String title, final String workload, final String start, final String finish) {
     super.clickOnMenu("Anonymous", "Public Workplans list");

     super.checkColumnHasValue(recordIndex, 0, title);
     super.checkColumnHasValue(recordIndex, 1, start);
     super.checkColumnHasValue(recordIndex, 2, finish);
     super.checkColumnHasValue(recordIndex, 3, workload);

     super.clickOnListingRecord(recordIndex);
     super.clickOnReturnButton("Return");
 }
	 
	 
	 @ParameterizedTest
	 @CsvFileSource(resources = "/anonymous/workplan/show.csv", encoding = "utf-8", numLinesToSkip = 1)
	 @Order(10)
	 public void ShowDetails(final int recordIndex, final String title, final String description, final String workload, final String start,
		 final String finish) {
		 super.clickOnMenu("Anonymous", "Public Workplans list");
		 super.clickOnListingRecord(recordIndex);

		 super.checkInputBoxHasValue("title", title);
		 super.checkInputBoxHasValue("description", description);
		 super.checkInputBoxHasValue("workload", workload);
		 super.checkInputBoxHasValue("startDateTime", start);
		 super.checkInputBoxHasValue("finishDateTime", finish);

		 super.clickOnReturnButton("Return");
	 }

}
