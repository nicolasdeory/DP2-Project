package acme.testing.anonymous.shout;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmeTest;

public class AnonymousCreateShoutTest extends AcmeTest {

    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(true);

    }
    // Test cases -------------------------------------------------------------
    // Ancillary methods ------------------------------------------------------
    
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/shout/CreatePositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void list(final int recordIndex, final String description, final String finish,final String start,
    	final String is_public, final String link, final String title, final String workload) {
        super.clickOnMenu("Anonymous", "Shout!");        
        
//        super.checkColumnHasValue(recordIndex, 0, title);
//        super.checkColumnHasValue(recordIndex, 1, start);
//        super.checkColumnHasValue(recordIndex, 2, finish);    
//        super.checkColumnHasValue(recordIndex, 3, workload);
//        super.checkColumnHasValue(recordIndex, 4, link);
//        
//        super.clickOnListingRecord(recordIndex);
//        
//        super.checkInputBoxHasValue("title", title);
//        super.checkInputBoxHasValue("description", description);        
//        super.checkInputBoxHasValue("public", is_public);
//        super.checkInputBoxHasValue("workload", workload);
//        super.checkInputBoxHasValue("start", start);
//        super.checkInputBoxHasValue("finish", finish);
//        super.checkInputBoxHasValue("link",link);
        
        
        super.signOut();
    }
}