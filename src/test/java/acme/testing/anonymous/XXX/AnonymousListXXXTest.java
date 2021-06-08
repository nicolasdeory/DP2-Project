package acme.testing.anonymous.XXX;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

@ExtendWith(MockitoExtension.class)
class AnonymousListXXXTest extends AcmeTest {

	
//	@Mock
//	private AnonymousShoutRepository sv;
	
//	@InjectMocks
//	private AnonymousShoutRepository sv;
	
    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();
        
        
        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(false);
        

    }
    // Test cases -------------------------------------------------------------
    
    // El test recibe una lista de shouts, en este caso es especial puesto que como solo muestra los que tienen menos de un mes de antiguedad
    // se ha decidido poblar dentro del test con uno de prueba para que sea cual sea el momento, funcione sin ningun problema
    // Primero creamos el shout, y tras ello vamos a la vista de listado y comprobamos que está el shout
    
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/XXX/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void list(final int recordIndex, final String author, final String info,
    	final String text,final String currency,final String flag) {
    	
        //creamos un shout ya que si no no funciona
     	super.clickOnMenu("Anonymous", "Shout!");


        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.fillInputBoxIn("currency",currency);
        if(flag!=null&&flag.equals("true"))super.clickAndGo(By.id("XXXflag$proxy"));
        super.clickOnSubmitButton("Shout!");
    	
        super.clickOnMenu("Anonymous", "List shouts");        
        
        super.checkColumnHasValue(recordIndex, 1, author);
        super.checkColumnHasValue(recordIndex, 2, text);
        super.checkColumnHasValue(recordIndex, 3, info);
        super.checkColumnHasValue(recordIndex,5,currency);
        super.checkColumnHasValue(recordIndex,6,flag);
        

    }
    
	@Test
	@Order(10)
	void ListNegativeCase() {

     		super.clickOnMenu("Anonymous", "Shout!");
     		final String url=super.driver.getCurrentUrl();
	 
			super.signIn("administrator", "administrator"); 
			super.clickOnMenu("Administrator", "Dashboard");
		
	     
	    	super.driver.navigate().to(url);
	    	super.checkErrorsExist();
	    	super.signOut();  
	    } 
    
    // Ancillary methods ------------------------------------------------------
    

}