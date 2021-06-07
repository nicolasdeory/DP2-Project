package acme.testing.anonymous.XXX;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class AnonymousCreateXXXTest extends AcmeTest {

    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(false);

    }
    // Test cases -------------------------------------------------------------
    //Para este test vamos a comprobar que los shouts se crean bien
    //Primero vamos a la pestaña de crear shout y rellenamos con los datos del csv
    //Vamos a la pestaña de listado de shouts y comprobamos que se ha creado correctamente
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/XXX/CreatePositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void CreatePositive(final int recordIndex, final String author, final String info,
    	final String text,String date,String currency,String flag) {
        super.clickOnMenu("Anonymous", "Shout!");        
        

        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.fillInputBoxIn("date",date);
        super.fillInputBoxIn("currency",currency);
        super.fillInputBoxIn("flag",flag);
        super.clickOnSubmitButton("Shout!");
        
        
        super.clickOnMenu("Anonymous", "List shouts"); 
        
        
        super.checkColumnHasValue(recordIndex, 1, author);
        super.checkColumnHasValue(recordIndex, 2, text);
        super.checkColumnHasValue(recordIndex, 3, info);
        super.checkColumnHasValue(recordIndex,4,date);
        super.checkColumnHasValue(recordIndex,5,currency);
        super.checkColumnHasValue(recordIndex,6,flag);
        
        
    }
    
    //Para este test vamos a comprobar que los shouts con errores no se crean
    //Primero vamos a la pestaña de crear shout y rellenamos con los datos del csv, que contienen errores 
    //como pueden ser campos vacíos, demasiado cortos, demasiado largos, con mal formato...
    //Tras ello comprueba la existencia de errores
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/XXX/CreateNegative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void CreateNegative(final int recordIndex, final String author, final String info,
    	final String text,String identifier,String currency,String flag) {
    	super.clickOnMenu("Anonymous", "Shout!");        
        

        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.fillInputBoxIn("XdateString",identifier);
        super.fillInputBoxIn("currency",currency);
        super.fillInputBoxIn("flag",flag);
        super.clickOnSubmitButton("Shout!");
        
        super.checkErrorsExist();
        
    }
    
    // Ancillary methods ------------------------------------------------------
}