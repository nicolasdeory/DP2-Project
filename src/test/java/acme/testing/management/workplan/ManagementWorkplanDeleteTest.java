package acme.testing.management.workplan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class ManagementWorkplanDeleteTest extends AcmeTest {

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

    //En este test vamos a comprobar que podemos borrar un workplan de manera correcta
    //Para ello vamos a iniciar sesion como manager y vamos al listado, clicamos en una y y pulsamos en el boton borrar
    //tras ello intentamos acceder de nuevo con la ruta y comprobamos que haya errores
    //Nos deslogueamos
    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    void deletePositive(final int recordIndex, final String title, final String description, final String isPublic,
            final String startDate, final String finishDate) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");

        super.checkColumnHasValue(1, 0, title);
        super.checkColumnHasValue(1, 2, startDate);
        super.checkColumnHasValue(1, 3, finishDate);

        super.clickOnListingRecord(1);
        final String path=super.getSimplePath();
        final String query=super.getContextQuery();
        super.checkButtonExists("Delete");
        super.clickOnReturnButton("Delete");

        super.checkColumnDoesNotHaveValue(1, 0, title);

        super.navigate(path,query);
        super.checkErrorsExist();

        super.signOut();
    }

    
    //En este test vamos a comprobar que podemos borrar un workplan de manera incorrecta
    //Para ello vamos a iniciar sesion como manager y vamos al listado, clicamos en una QUE ESTÉ FINALIZADA y y buscamos el boton borrar
    //Ese boton no existe por lo que pulsaremos en volver y comprobaremos que sigue en la lista
    //Nos deslogueamos
    @ParameterizedTest
    @CsvFileSource(resources = "/management/workplan/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(3)
    void deleteNegative(final int recordIndex, final String title, final String description, final String isPublic,
            final String startDate, final String finishDate) {
        super.signIn("juan21", "1234");
        super.clickOnMenu("Management", "My Workplans List");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 2, startDate);
        super.checkColumnHasValue(recordIndex, 3, finishDate);

        super.clickOnListingRecord(recordIndex);
        super.checkButtonDoesNotExist("Delete");
        super.clickOnReturnButton("Return");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 2, startDate);
        super.checkColumnHasValue(recordIndex, 3, finishDate);

        super.signOut();
    }
}
