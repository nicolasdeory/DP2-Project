package acme.testing.anonymous.promote;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class BecomeManagerTest extends AcmeTest {
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
    //Para este test vamos a comprobar que nos podemos convertir en manager de manera positiva. También que podemos actualizar el equipo.
    //Primero, nos registraremos e iniciaremos sesión
    //Haremos clic en la pestaña de hacernos manager y registraremos el equipo
    //Iremos a la pestaña de datos y comprobaremos que el valor es correcto.
    
    //Ahora actualizaremos el equipo con otro nombre y hacemos click en el boton
    //Volvemos a entrar y comprobamos que los valores se han actualizado
    //También comprobamos que tenemos las opciones en el menú de tasks y workplans, tanto listar los propios como crear
    @ParameterizedTest
    @CsvFileSource(resources = "/promote/become-manager-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void positiveBecomeManagerAndUpdateTeam(final String recordIndex, final String team) {

        this.signUp(this.username + recordIndex, this.password, this.name + recordIndex, this.surname, recordIndex + this.email);
        this.signIn(this.username + recordIndex, this.password);

        super.clickOnMenu("Account", "Become a management");
        super.fillInputBoxIn("team", team);
        super.clickOnSubmitButton("Register");

        super.clickOnMenu("Account", "Management data");
        super.checkInputBoxHasValue("team", team);

        super.fillInputBoxIn("team", team + " " + this.username);
        super.clickOnSubmitButton("Update");
        super.clickOnMenu("Account", "Management data");
        super.checkInputBoxHasValue("team", team + " " + this.username);
        super.clickOnReturnButton("Return");

        super.clickOnMenu("Management", "My Workplans List");
        super.clickOnMenu("Management", "Create new Workplan");
        super.clickOnMenu("Management", "My tasks");
        super.clickOnMenu("Management", "Create task");

        this.signOut();
    }

    //Para este test comprobaremos que si el formulario tiene errores no nos convertiremos en manager
    //Hacemos como en el positivo, nos registramos e iniciamos sesión y clicamos en convertirse en manager
    //Pero en el formulario pondremos palabras spam, texto demasiado corto o demasiado largo y comprobamos si existen errores
    @ParameterizedTest
    @CsvFileSource(resources = "/promote/become-manager-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    void negativeBecomeManager(final String recordIndex, final String team) {

        this.signUp(this.username + recordIndex, this.password, this.name + recordIndex, this.surname, recordIndex + this.email);
        this.signIn(this.username + recordIndex, this.password);

        super.clickOnMenu("Account", "Become a management");
        super.fillInputBoxIn("team", team);
        super.clickOnSubmitButton("Register");

        super.checkErrorsExist();
    }
}
