package acme.testing.accounts;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class UpdateUserAccountTest extends AcmeTest {

    // Internal state ---------------------------------------------------------
    String actualPassword = "1234";
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
        this.sleep(10, true);
    }

    // Test cases -------------------------------------------------------------

    /*
     * CSV: positive signup cases;
     * 
     * TEST: 1: sign in, 2: go to update account, 3: fill the fields, 4:update, 5:
     * sign out, 6: sign in updated, 7: check new values
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/sign-up/positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void positiveUpdate(final String username, final String password, final String name, final String surname,
            final String email) {
        this.signIn("juan21", this.actualPassword);

        super.clickOnMenu("Account", "General data");

        super.fillInputBoxIn("password", password);
        super.fillInputBoxIn("confirmation", password);
        super.fillInputBoxIn("identity.name", name);
        super.fillInputBoxIn("identity.surname", surname);
        super.fillInputBoxIn("identity.email", email);

        super.clickOnSubmitButton("Update");

        this.signOut();
        this.signIn("juan21", password);

        super.clickOnMenu("Account", "General data");

        super.checkInputBoxHasValue("username", "juan21");
        super.checkInputBoxHasValue("identity.name", name);
        super.checkInputBoxHasValue("identity.surname", surname);
        super.checkInputBoxHasValue("identity.email", email);
        this.actualPassword = password;
        this.signOut();
    }

    /*
     * CSV: positive signup cases;
     * 
     * TEST: 1: sign in, 2: go to update account, 3: fill the fields, 4:update, 5:
     * sign out, 6: sign in updated, 7: check new values
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/sign-up/negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void negativeUpdate(final String username, final String password, final String name, final String surname,
            final String email) {
        this.signIn("juan21", this.actualPassword);

        super.clickOnMenu("Account", "General data");

        super.fillInputBoxIn("password", password);
        super.fillInputBoxIn("confirmation", password);
        super.fillInputBoxIn("identity.name", name);
        super.fillInputBoxIn("identity.surname", surname);
        super.fillInputBoxIn("identity.email", email);
        super.clickOnSubmitButton("Update");

        super.checkErrorsExist();

        this.signOut();
    }

    // Ancillary methods ------------------------------------------------------

}
