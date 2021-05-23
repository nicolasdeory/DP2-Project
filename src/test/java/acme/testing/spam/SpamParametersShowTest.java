package acme.testing.spam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class SpamParametersShowTest extends AcmeTest {

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
    }
    //Para este test debemos loguearnos como administrador y vamos a parametros de spam
    //Comprobamos que el input de el umbral es el dado
    @Test
    void showThreshold() {
        this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.checkInputBoxHasValue("threshold", "0.10");
    }

    //Para este test debemos loguearnos como administrador y vamos a parametros de spam
    //Comprobamos que el listado de palabras clave es igual que el dado 
    @ParameterizedTest
    @CsvFileSource(resources = "/spam/show.csv", encoding = "utf-8", numLinesToSkip = 1)
    void showKeywords(final int recordIndex, final String keyword) {
        this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkExists(By.cssSelector("option[value=\"" + keyword + "\"]"));
    }
}
