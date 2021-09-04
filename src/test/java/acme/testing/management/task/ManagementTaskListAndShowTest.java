package acme.testing.management.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class ManagementTaskListAndShowTest extends AcmeTest {
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
        super.sleep(10,true);

    }
    // Test cases -------------------------------------------------------------
    //Para este test comprobamos que el listado sea identico al dado
    //Entramos como administrador y vamos a la pantalla de mis tareas
    //Comprobamos cada elemento de la tabla y clicamos en cada uno comprobando asi los inputs de la vista de show
    //Nos deslogeamos
    
    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/listMyTasks.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void listAndShowMyTasksPositive(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                             final String link) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My tasks");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, description);
        super.checkColumnHasValue(recordIndex, 2, start);
        super.checkColumnHasValue(recordIndex, 3, finish);
        super.checkColumnHasValue(recordIndex, 4, workload);
        super.checkColumnHasValue(recordIndex, 5, link);

        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("isPublic", isPublic);
        super.checkInputBoxHasValue("startDateTime", start);
        super.checkInputBoxHasValue("finishDateTime", finish);
        super.checkInputBoxHasValue("workload",workload);
        super.checkInputBoxHasValue("link",link);

        super.signOut();
    }

    // Test cases -------------------------------------------------------------
    //Para este test comprobamos que el listado sea identico al dado
    //Entramos como administrador y vamos a la pantalla de tareas finalizadas
    //Comprobamos cada elemento de la tabla y clicamos en cada uno comprobando asi los inputs de la vista de show
    //Nos deslogeamos
    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/listFinished.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void listAndShowFinishedPositive(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                            final String link) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Authenticated", "Finished Tasks");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, start);
        super.checkColumnHasValue(recordIndex, 2, finish);
        super.checkColumnHasValue(recordIndex, 3, workload);
        super.checkColumnHasValue(recordIndex, 4, link);

        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("isPublic", isPublic);
        super.checkInputBoxHasValue("startDateTime", start);
        super.checkInputBoxHasValue("finishDateTime", finish);
        super.checkInputBoxHasValue("workload",workload);
        super.checkInputBoxHasValue("link",link);

        super.signOut();
    }
    
    
    //Para este test hacemos login como administrador y clicamos en una tarea
    //Copiamos la ruta y la query realizada, nos deslogueamos
    //intentamos entrar como otro usuario y comprobamos si saltan errores
    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/listMyTasks.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void listAndShowMyTasksNegative(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                            final String link) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My tasks");

        super.clickOnListingRecord(recordIndex);

        String url=super.driver.getCurrentUrl();

        super.signOut();
        super.signIn("lola21","1234");

        super.driver.navigate().to(url);
        super.checkErrorsExist();

        super.signOut();
    }
}
