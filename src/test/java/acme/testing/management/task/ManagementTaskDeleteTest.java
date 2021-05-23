package acme.testing.management.task;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

public class ManagementTaskDeleteTest extends AcmeTest {
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
        super.sleep(10,true);

    }
    // Test cases -------------------------------------------------------------
    //En este test se prueba a eliminar una task y comprobar que esta ya no se encuentra en el listado
    //Se espera que la task se elimine satisfactoriamente
    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void deletePositive(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
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

        super.clickOnSubmitButton("Delete");

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/management/task/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void deleteNegative(final int recordIndex, final String title,final String workload,final String description,final String isPublic, final String start, final String finish,
                               final String link) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My tasks");

        super.clickOnListingRecord(recordIndex);

        super.checkNotExists(By.linkText("Delete"));


        super.signOut();
    }
}
