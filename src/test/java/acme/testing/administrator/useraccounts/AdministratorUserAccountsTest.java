package acme.testing.administrator.useraccounts;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.core.annotation.Order;

import acme.testing.AcmeTest;

public class AdministratorUserAccountsTest extends AcmeTest{

	 // Lifecycle management ---------------------------------------------------
	@Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();
        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(false);
        
    }
    // Test cases -------------------------------------------------------------
    // Ancillary methods ------------------------------------------------------
    
    @ParameterizedTest
    @CsvFileSource(resources = "/administrator/useraccount/listPositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void listPositive(final int recordIndex, final String username, final String identity_name,
    	final String identity_surname) {
    	this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "User accounts");        
        
        super.checkColumnHasValue(recordIndex,0 , username); 
		super.checkColumnHasValue(recordIndex,1 , identity_name); 
		super.checkColumnHasValue(recordIndex,2 , identity_surname); 
    }
    
    
    @ParameterizedTest
    @CsvFileSource(resources = "/administrator/useraccount/showPositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void showPositive(final int recordIndex, final String username, final String identity_name,
    	final String identity_surname, final String identity_email, final String role, final String enabled) {
    	this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "User accounts");        
        
        super.clickOnListingRecord(recordIndex); 
        
        super.checkInputBoxHasValue("username", username); 
        super.checkInputBoxHasValue("identity.name", identity_name); 
        super.checkInputBoxHasValue("identity.surname", identity_surname); 
        super.checkInputBoxHasValue("identity.email", identity_email); 
        super.checkInputBoxHasValue("roleList", role); 
        super.checkInputBoxHasValue("status",enabled); 
    }
    
	
}
