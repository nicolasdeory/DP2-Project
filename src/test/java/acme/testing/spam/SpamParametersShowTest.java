package acme.testing.spam;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

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

    @Test
    void showThreshold() {
        this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");

        super.checkInputBoxHasValue("threshold", "0.10");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/spam/show.csv", encoding = "utf-8", numLinesToSkip = 1)
    void showKeywords(final int recordIndex, final String keyword) {
        this.signIn("administrator", "administrator");
        super.clickOnMenu("Administrator", "Spam parameters");
        super.checkExists(By.cssSelector("option[value=\"" + keyword + "\"]"));
    }
}
