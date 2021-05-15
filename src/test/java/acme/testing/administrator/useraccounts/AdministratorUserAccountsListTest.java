package acme.testing.administrator.useraccounts;

import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

public class AdministratorUserAccountsListTest extends AcmeTest{

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
        this.signOut();
    }
    // Test cases -------------------------------------------------------------
    // Ancillary methods ------------------------------------------------------
    
//    @ParameterizedTest
//    @CsvFileSource(resources = "/anonymous/shout/list.csv", encoding = "utf-8", numLinesToSkip = 1)
//    @Order(10)
//    public void list(final int recordIndex, final String author, final String info,final String moment,
//    	final String text) {
//        super.clickOnMenu("Administrator", "User accounts");        
//
//    }
	
}
