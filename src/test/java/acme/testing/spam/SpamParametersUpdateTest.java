package acme.testing.spam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import acme.testing.AcmeTest;

class SpamParametersUpdateTest extends AcmeTest {
    // Lifecycle management --------------------------------------------------- 
    @Override
    @BeforeAll
    public void beforeAll() {
        super.beforeAll();

        super.setBaseCamp("http", "localhost", "8080", "/Acme-WorkPlans", "/master/welcome", "?language=en&debug=true");
        super.setAutoPausing(false);
        super.signIn("administrator", "administrator");
        super.clickAndGo(By.linkText("Administrator"));
        super.clickAndGo(By.linkText("Populate DB (initial)"));
        super.checkAlertExists(true);
    }
    // Test cases -------------------------------------------------------------
    
    //Para este test vamos a actualizar el umbral de manera correcta
    //Haremos login como administrador y entraremos a la pestaña de parametros de spam
    //Tras ello, buscaremos el input a actualizar y cambiaremos con el dado en el csv
    //Haremos click en el boton de actualizar, volveremos a entrar a la pestaña y comprobaremos que se ha actualizado de manera correcta
    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/spam/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void updateThresholdPositive(final int recordIndex, final String threshold, final String expectedThreshold) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("threshold", threshold);
        super.clickOnSubmitButton("Update");

        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkInputBoxHasValue("threshold", expectedThreshold);
    }
    
    //Para este test vamos a actualizar el umbral de manera incorrecta
    //Haremos login como administrador y entraremos a la pestaña de parametros de spam
    //Tras ello, buscaremos el input a actualizar y cambiaremos con el dado en el csv que contienen errores (numeros negativos, superiores a 1, texto...)
    //Haremos click en el boton de actualizar, y comprobaremos que existen errores
    @ParameterizedTest
    @CsvFileSource(resources = "/spam/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    void updateThresholdNegative(final int recordIndex, final String threshold) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("threshold", threshold);
        super.clickOnSubmitButton("Update");
        super.checkErrorsExist();
    }

    //Para este test vamos a actualizar las palabras clave de manera correcta
    //Haremos login como administrador y entraremos a la pestaña de parametros de spam
    //Tras ello, buscaremos el input a actualizar y cambiaremos con el dado en el csv, haremos clic en añadir palabra
    //Haremos click en el boton de actualizar, volveremos a entrar a la pestaña y comprobaremos que se ha actualizado de manera correcta
    @ParameterizedTest
    @CsvFileSource(resources = "/spam/add-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void addKeywordPositive(final int recordIndex, final String keyword) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("newKeyword", keyword);
        super.clickAndGo(By.id("add-keyword"));
        super.clickOnSubmitButton("Update");

        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkExists(By.cssSelector("option[value=\"" + keyword + "\"]"));
    }
    
    //Para este test vamos a actualizar las palabras clave de manera incorrecta
    //Haremos login como administrador y entraremos a la pestaña de parametros de spam
    //Tras ello, buscaremos el input a actualizar y cambiaremos con el dado en el csv, haremos clic en añadir palabra
    //Haremos click en el boton de actualizar, volveremos a entrar a la pestaña y comprobaremos que saltan errores
    @ParameterizedTest
    @CsvFileSource(resources = "/spam/add-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    void addKeywordNegative(final int recordIndex, final String keyword) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.fillInputBoxIn("newKeyword", keyword);
        super.clickAndGo(By.id("add-keyword"));
        super.clickOnSubmitButton("Update");

        super.checkErrorsExist();
    }

    //Para este test vamos a eliminar palabras de manera correcta
    //Haremos login como administrador y entraremos a la pestaña de parametros de spam
    //Tras ello, buscaremos la palabra en el listado, la seleccionaremos y haremos click en remove
    //Haremos click en el boton de actualizar, volveremos a entrar a la pestaña y comprobaremos que ya no está listada
    @CsvFileSource(resources = "/spam/remove.csv", encoding = "utf-8", numLinesToSkip = 1)
    void removeKeyword(final int recordIndex, final String keyword) {
        super.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        final Select select = new Select(super.driver.findElement(By.id("keywords")));
        select.selectByValue(keyword);
        super.clickAndGo(By.id("remove-keyword"));
        super.clickOnSubmitButton("Update");
        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkNotExists(By.xpath("//select/option[@value='" + keyword + "']"));
    }
} 
