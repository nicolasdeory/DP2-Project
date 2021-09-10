package acme.testing.anonymous.gusit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class AdministratorDashboardGusitShowTest extends AcmeTest {

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
		this.signOut();
	}

	/**
	 * Test para comprobar la funcionalidad para Administrador del Dashboard. El
	 * Dashboard muestra información al administrador sobre una gran cantidad de
	 * datos obtenidos a través de distintas queries y funciones de distintos
	 * elementos del proyecto. En este test se comprobará que exactamente el dato
	 * del dashboard se corresponde al indicado en el test.
	 */
	@Test
	@Order(20)
	void show() {
		this.signIn("administrator", "administrator");
		super.clickOnMenu("Administrator", "Dashboard");

		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[1]/td[normalize-space(text()) = '2.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[2]/td[normalize-space(text()) = '0.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[3]/td[normalize-space(text()) = '6.12']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[4]/td[normalize-space(text()) = '0.82']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[5]/td[normalize-space(text()) = '-1.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[6]/td[normalize-space(text()) = '-1.00']"));

		this.signOut();

	}

	// Este test es el caso negativo de show y para ello vamos a intentar entrar
	// como usuario anónimo.
	// Primero iniciamos sesión como administrador y entramos en la dashboard
	// Tras ello guardamos la ruta y la query
	// Nos desloagueamos e intentamos acceder como anonimo y comprobamos que saltan
	// errores.
	@Test
	@Order(10)
	void showNegativeCase() {
		super.signIn("administrator", "administrator");
		super.clickOnMenu("Administrator", "Dashboard");

		final String url = super.driver.getCurrentUrl();

		super.signOut();
		super.driver.navigate().to(url);
		super.checkErrorsExist();
	}
}
