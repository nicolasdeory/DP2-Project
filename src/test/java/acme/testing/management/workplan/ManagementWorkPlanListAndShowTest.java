package acme.testing.management.workplan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class ManagementWorkPlanListAndShowTest extends AcmeTest {

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
    
    //En este test vamos a comprobar que el listado y la vista show funcionen correctamente
    //Para ello nos loguearemos como manager y entramos al listado
    //comprobamos que los valores de las columnaws son correctos y entramos en ella
    //De nuevo, comprobamos los valores, en este caso de los inputs
    //Nos deslogueamos 
    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/listAndShow.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    void listAndShowPositive(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
        super.signIn("juan21", "1234");

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

    //En este test vamos a comprobar que el listado y la vista show funcionen incorrectamente
    //Para ello nos loguearemos como manager y entramos al listado
    //Copiaremos la ruta del show del workplan
    //Nos deslogueamos e intentamos entrar a esa vista desde otro usuario
    //Comprobamos que existen errores y nos volvemos a desloguear
    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/listAndShow.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    void listAndShowNegative(final int recordIndex, final String title, final String description, final String isPublic, final String startDate, final String finishDate,final String tasks) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");

        super.clickOnListingRecord(recordIndex);

        final String path=super.getSimplePath();
        final String query=super.getContextQuery();

        super.signOut();
        super.signIn("lola21","1234");

        super.navigate(path,query);
        super.checkErrorsExist();

        super.signOut();
    }
}
