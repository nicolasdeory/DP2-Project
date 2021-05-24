package acme.testing.management.workplan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class ManagementWorkplanCreateTest extends AcmeTest {

    // Lifecycle management ---------------------------------------------------
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-Planner", "/master/welcome", "?language=en&debug=true");
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
    //Para este test intentamos crear workplans de manera correcta
    //Entraremos como manager y pulsaremos como crear nuevo workplan
    //Rellenamos los datos obtenidos del csv y pulsamos en crear 
    //Tras ello entramos al listado y comprobamos que se ha creado correctamente, y entramos en el y comprobamos los inputs
    //nos deslogueamos
    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    void createPositive(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
        super.signIn("rosa21", "1234");

        super.clickOnMenu("Management", "Create new Workplan");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        if(isPublic.equals("true"))super.clickAndGo(By.id("isPublic$proxy"));
        if(tasks!=null){
            final String s[]=tasks.split(";");
            for(final String task:s){
                super.clickAndGo(By.id(task));
            }
        }


        super.fill(By.id("startDateTime"), startDate);
        super.fill(By.id("finishDateTime"), finishDate);
        super.clickOnSubmitButton("Create");


        super.clickOnMenu("Management", "My Workplans List");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 2, startDate);
        super.checkColumnHasValue(recordIndex, 3, finishDate);

        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("isPublic", isPublic);
        super.checkInputBoxHasValue("startDateTime", startDate);
        super.checkInputBoxHasValue("finishDateTime", finishDate);
        if(tasks!=null){
            final String s[]=tasks.split(";");
            for(final String task:s){
                assert super.driver.findElement(By.id(task)).isSelected();
            }
        }

        super.signOut();
    }
    //Para este test intentamos crear workplans de manera incorrecta
    //Entraremos como manager y pulsaremos como crear nuevo workplan
    //Rellenamos los datos del csv que contienen fallos como inputs vacios, formatos errones... y pulsamos en crear
    //comprobamos que existen errores
    //nos deslogueamos
    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    void createNegative(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
        super.signIn("rosa21", "1234");

        super.clickOnMenu("Management", "Create new Workplan");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        if(isPublic.equals("true"))super.clickAndGo(By.id("isPublic$proxy"));
        if(tasks!=null){
            final String s[]=tasks.split(";");
            for(final String task:s){
                super.clickAndGo(By.id(task));
            }
        }
        super.fill(By.id("startDateTime"), startDate);
        super.fill(By.id("finishDateTime"), finishDate);
        super.clickOnSubmitButton("Create");
        super.checkErrorsExist();

        super.signOut();
    }


}
