package acme.testing.administrator.useraccounts;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.springframework.core.annotation.Order;

import acme.testing.AcmeTest;

class AdministratorUserAccountsTest extends AcmeTest{

	 // Lifecycle management ---------------------------------------------------
	@Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-WorkPlans", "/master/welcome", "?language=en&debug=true");
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
   
    //En este test vamos a comprobar que el listado de usuarios sea correcto al dado
	//Para ello iniciamos sesión como administrador y vamos a la pestaña de cuentas de usuario
	//Y comprobamos que el listado sea correcto en usuario, nombre y apellido
    @ParameterizedTest
    @CsvFileSource(resources = "/administrator/useraccount/listPositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void listPositive(final int recordIndex, final String username, final String identity_name,
    	final String identity_surname) {
    	this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "User accounts");        
        
        super.checkColumnHasValue(recordIndex,0 , username); 
		super.checkColumnHasValue(recordIndex,1 , identity_name); 
		super.checkColumnHasValue(recordIndex,2 , identity_surname); 
    }
    
    //En este test vamos a comprobar que el show  de usuarios sea correcto al dado
	//Para ello iniciamos sesión como administrador y vamos a la pestaña de cuentas de usuario
	//Hacemos click y vamos comprobando los valores de los inputs frente a los que vienen en la tabla
    
    @ParameterizedTest
    @CsvFileSource(resources = "/administrator/useraccount/showPositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void showPositive(final int recordIndex, final String username, final String identity_name,
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
    
    //En este test vamos a comprobar que el listado de usuarios y el show no sean accesibles sin tener el rol concreto
	//Para ello iniciamos sesión como administrador y vamos a la pestaña de cuentas de usuario
	//Hacemos clic en la fila de la tabla correspondiente y guardamos la ruta y la query
    //Nos deslogueamos e intentamos entrar, lo que nos llevaría a error
    @ParameterizedTest
    @CsvFileSource(resources = "/administrator/useraccount/showPositive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(3)
	void listAndShowNegative(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
    	this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "User accounts");  
        super.clickOnListingRecord(recordIndex);

        String url=super.driver.getCurrentUrl();

        super.signOut();
	    super.signIn("juan21","1234");

	    super.driver.navigate().to(url);
	    super.checkErrorsExist();

	    super.signOut();
    }
    
    
    // Ancillary methods ------------------------------------------------------
	
}
