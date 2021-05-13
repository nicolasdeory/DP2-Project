package management.workplan;

import acme.testing.AcmeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public abstract class ManagementWorkplanCreateTest extends AcmeTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/managament/workplan/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void createPositive(final int recordIndex,String title,String description, String isPublic,String startDate,String finishDate) {
        super.signIn("juan21", "1234");

        super.clickOnMenu("Managament", "Create a workplan");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        super.fillInputBoxIn("isPublic", isPublic);
        super.fillInputBoxIn("startDate", startDate);
        super.fillInputBoxIn("finishDate", finishDate);
        super.clickOnSubmitButton("Create");

        super.clickOnMenu("Managament", "List my workplans");

        super.checkColumnHasValue(recordIndex, 0, title);
        super.checkColumnHasValue(recordIndex, 1, startDate);
        super.checkColumnHasValue(recordIndex, 2, finishDate);

        super.clickOnListingRecord(recordIndex);

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("description", description);
        super.checkInputBoxHasValue("isPublic", isPublic);
        super.checkInputBoxHasValue("startDate", startDate);
        super.checkInputBoxHasValue("finishDate", finishDate);

        super.signOut();
    }


}
