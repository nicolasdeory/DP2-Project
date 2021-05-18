package acme.testing.anonymous.promote;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

public class BecomeManagerTest extends AcmeTest {
    // Internal state ---------------------------------------------------------
    String username = "elocorch";
    String name = "Elo";
    String surname = "Corch";

    String password = "123123123";
    String email = "corch@u.es";
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

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/promote/become-manager-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void positiveBecomeManagerAndUpdateTeam(final String recordIndex, final String team) {

        this.signUp(username + recordIndex, password, name + recordIndex, surname, recordIndex + email);
        this.signIn(username + recordIndex, password);

        super.clickOnMenu("Account", "Become a management");
        super.fillInputBoxIn("team", team);
        super.clickOnSubmitButton("Register");

        super.clickOnMenu("Account", "Management data");
        super.checkInputBoxHasValue("team", team);

        super.fillInputBoxIn("team", team + " " + username);
        super.clickOnSubmitButton("Update");
        super.clickOnMenu("Account", "Management data");
        super.checkInputBoxHasValue("team", team + " " + username);
        super.clickOnReturnButton("Return");

        super.clickOnMenu("Management", "My Workplans List");
        super.clickOnMenu("Management", "Create new Workplan");
        super.clickOnMenu("Management", "My tasks");
        super.clickOnMenu("Management", "Create task");

        this.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/promote/become-manager-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void negativeBecomeManager(final String recordIndex, final String team) {

        this.signUp(username + recordIndex, password, name + recordIndex, surname, recordIndex + email);
        this.signIn(username + recordIndex, password);

        super.clickOnMenu("Account", "Become a management");
        super.fillInputBoxIn("team", team);
        super.clickOnSubmitButton("Register");

        super.checkErrorsExist();
    }
}
