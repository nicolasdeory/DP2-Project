package acme.testing.anonymous.shout;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.junit.jupiter.MockitoExtension;

import acme.testing.AcmeTest;

@ExtendWith(MockitoExtension.class)
public class AnonymousListShoutTest extends AcmeTest {

	
//	@Mock
//	private AnonymousShoutRepository sv;
	
//	@InjectMocks
//	private AnonymousShoutRepository sv;
	
    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

//        MockitoAnnotations.initMocks(this);
        
        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(true);
        
//        final List<Shout> shouts = new ArrayList<>();
//        final Shout s = new Shout();
//        final Calendar c = Calendar.getInstance();
//        c.set(2021, 4, 1);
//        s.setAuthor("Fernandoedasredw");
//        s.setMoment(c.getTime());
//        s.setId(1);
//        s.setText("Let's goos");
//        shouts.add(s);
////        Mockito.when(this.sv.findMany(ArgumentMatchers.<Request<Shout>>any())).thenReturn(shouts);
//        Mockito.when(this.sv.findMany(ArgumentMatchers.any(Date.class))).thenReturn(shouts);

    }
    // Test cases -------------------------------------------------------------
    // Ancillary methods ------------------------------------------------------
    
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/shout/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void list(final int recordIndex, final String author, final String info,final String moment,
    	final String text) {
        super.clickOnMenu("Anonymous", "List shouts");        
        
        super.checkColumnHasValue(recordIndex, 0, moment);
        super.checkColumnHasValue(recordIndex, 1, author);
        super.checkColumnHasValue(recordIndex, 2, text);    
        super.checkColumnHasValue(recordIndex, 3, info);
        

    }
}