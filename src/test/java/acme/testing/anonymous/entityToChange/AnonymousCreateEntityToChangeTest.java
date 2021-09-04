package acme.testing.anonymous.entityToChange;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class AnonymousCreateEntityToChangeTest extends AcmeTest {

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

    }

    // Test cases -------------------------------------------------------------
    // Para este test vamos a comprobar que los shouts se crean bien
    // Primero vamos a la pestaña de crear shout y rellenamos con los datos del csv
    // Vamos a la pestaña de listado de shouts y comprobamos que se ha creado
    // correctamente
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/entityToChange/CreatePositive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void CreatePositive(final int recordIndex, final String author, final String info, final String text,
            final String moneyAttributeToChange, final String flag) {
        super.clickOnMenu("Anonymous", "Shout!");

        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.fillInputBoxIn("moneyAttributeToChange", moneyAttributeToChange);
        if (flag != null && flag.equals("true"))
            super.clickAndGo(By.id("flagAttributeToChange$proxy"));
        super.clickOnSubmitButton("Shout!");

        super.clickOnMenu("Anonymous", "List shouts");

        super.checkColumnHasValue(recordIndex, 1, author);
        super.checkColumnHasValue(recordIndex, 2, text);
        super.checkColumnHasValue(recordIndex, 3, info);
        super.checkColumnHasValue(recordIndex, 5, moneyAttributeToChange);
        super.checkColumnHasValue(recordIndex, 6, flag);

    }

    // Para este test vamos a comprobar que los shouts con errores no se crean
    // Primero vamos a la pestaña de crear shout y rellenamos con los datos del csv,
    // que contienen errores
    // como pueden ser campos vacíos, demasiado cortos, demasiado largos, con mal
    // formato...
    // Tras ello comprueba la existencia de errores
    @ParameterizedTest
    @CsvFileSource(resources = "/anonymous/entityToChange/CreateNegative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void CreateNegative(final int recordIndex, final String author, final String info, final String text,
            final String moneyAttributeToChange, final String flag) {
        super.clickOnMenu("Anonymous", "Shout!");

        super.fillInputBoxIn("author", author);
        super.fillInputBoxIn("text", text);
        super.fillInputBoxIn("info", info);
        super.fillInputBoxIn("moneyAttributeToChange", moneyAttributeToChange);
        if (flag != null && flag.equals("true"))
            super.clickAndGo(By.id("flagAttributeToChange$proxy"));
        super.clickOnSubmitButton("Shout!");

        super.checkErrorsExist();

    }

    // Ancillary methods ------------------------------------------------------
}