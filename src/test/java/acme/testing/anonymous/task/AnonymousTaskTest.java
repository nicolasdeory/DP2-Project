package acme.testing.anonymous.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class AnonymousTaskTest extends AcmeTest {

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

    // Para este test comprobaremos que la lista es correcta.
    // Primero entraremos en la pestaña de tareas publicas, e iremos comprobando una
    // a una los valores de las columnas
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/task/list.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void listAndShow(final int recordIndex, final String title, final String start, final String finish,
            final String workload, final String link) {
        super.clickOnMenu("Anonymous", "Public Tasks");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, start);
        super.checkColumnHasValue(recordIndex, 2, finish);
        super.checkColumnHasValue(recordIndex, 3, workload);
        super.checkColumnHasValue(recordIndex, 4, link);

        super.clickOnListingRecord(recordIndex);
        super.clickOnReturnButton("Return");
    }

    // En este caso comprobaremos que todos los valores de la lista dada
    // corresponden con los inputs de la task en cuestión
    // Para ello, entraremos a la vista de public task e iremos clicando en cada una
    // de ellas para ir a la vista del show
    // En esta, iremos comprobando que los inputs se corresponden y son correctos
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/task/show.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void ShowDetails(final int recordIndex, final String title, final String description, final String start,
            final String finish, final String workload, final String link) {
        super.clickOnMenu("Anonymous", "Public Tasks");
        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("workload", workload);
        super.checkInputBoxHasValue("isPublic", "true");
        super.checkInputBoxHasValue("startDateTime", start);
        super.checkInputBoxHasValue("finishDateTime", finish);
        super.checkInputBoxHasValue("link", link);

        super.clickOnReturnButton("Return");
    }

    /*
     * CSV: ids de tareas públicas terminadas
     * 
     * Test: 1: obtener la url base de show public open task, 2: usar dicha url para
     * intentar mostrar tareas públicas terminadas y tareas privadas, 3: comprobar
     * que llegamos a un error
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/task/list-show-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void listAndShowNegative(final int id) {
        super.clickOnMenu("Anonymous", "Public Tasks");
        super.clickOnListingRecord(0);

        final String path = super.getSimplePath();

        super.navigate(path, "id=" + id);
        super.checkErrorsExist();

    }

    // Ancillary methods ------------------------------------------------------
}
