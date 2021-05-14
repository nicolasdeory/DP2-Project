package acme.testing.anonymous.task;

import java.util.Date;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.SignUpTest;

public class AnonymousPublicTaskListTest extends SignUpTest {

	// Lifecycle management ---------------------------------------------------
	// Test cases -------------------------------------------------------------
	// Ancillary methods ------------------------------------------------------
	
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/task/list.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void list(int recordIndex,int id, int version, String description, Date finish,Date start,Boolean is_public, String link, String title, double workload, int user_id ) {
		
		super.clickOnMenu("Anonymous", "Public Tasks");		
		
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, execution_period_start_date_time);
		super.checkColumnHasValue(recordIndex, 2, execution_period_finish_date_time);
		super.checkColumnHasValue(recordIndex, 3, workload);
		super.checkColumnHasValue(recordIndex, 4, link);
		super.checkColumnHasValue(recordIndex, 5, title);
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("description", description);		
		super.checkInputBoxHasValue("public", is_public);
		super.checkInputBoxHasValue("workload", workload);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("finish", finish);
		super.checkInputBoxHasValue("link",link);
		
		super.signOut();
	}
}
