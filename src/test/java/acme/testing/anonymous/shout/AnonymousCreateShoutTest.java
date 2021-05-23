package acme.testing.anonymous.shout;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmeTest;

class AnonymousCreateShoutTest extends AcmeTest {

    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(false);

    }
    // Test cases -------------------------------------------------------------
    
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/shout/CreatePositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void CreatePositive(final int recordIndex, final String author, final String info,
    	final String text) {
        super.clickOnMenu("Anonymous", "Shout!");        
        

        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.clickOnSubmitButton("Shout!");
        
//        final LocalDateTime fecha = LocalDateTime.now();
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");  
//        final String fechaparseada = fecha.format(formatter);
        

        
        super.clickOnMenu("Anonymous", "List shouts"); 
        
        
        super.checkColumnHasValue(0, 1, author);
        super.checkColumnHasValue(0, 2, text);    
        super.checkColumnHasValue(0, 3, info);
        
        
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/shout/CreateNegative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void CreateNegative(final int recordIndex, final String author, final String info,
    	final String text) {
    	super.clickOnMenu("Anonymous", "Shout!");        
        

        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.clickOnSubmitButton("Shout!");
        
        
        super.checkErrorsExist();
        
    }
    
    // Ancillary methods ------------------------------------------------------
}