package acme.testing.administrator.dashboard;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

public class AdministratorDashboardShowTest extends AcmeTest {
	
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
        this.sleep(10, true);
        this.signOut();
	}

	/**
	 * Test para comprobar la funcionalidad para Administrador del Dashboard.
	 * El Dashboard muestra información al administrador sobre una gran cantidad de datos obtenidos
	 * a través de distintas queries y funciones de
	 * distintos elementos del proyecto.
	 * En este test se comprobará que exactamente el dato del dashboard se corresponde al indicado en el test.
	 */
	@Test
	@Order(20)
	void show() {
		this.signIn("administrator", "administrator");
		super.clickOnMenu("Administrator", "Dashboard");		
		
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[1]/td[normalize-space(text()) = '11']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[2]/td[normalize-space(text()) = '3']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[3]/td[normalize-space(text()) = '2']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[4]/td[normalize-space(text()) = '12']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[5]/td[normalize-space(text()) = '1,341.55']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[6]/td[normalize-space(text()) = '715.46']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[7]/td[normalize-space(text()) = '2.13']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[8]/td[normalize-space(text()) = '2,803.52']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[9]/td[normalize-space(text()) = '1.55']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[10]/td[normalize-space(text()) = '0.26']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[11]/td[normalize-space(text()) = '192.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[12]/td[normalize-space(text()) = '9,355.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[13]/td[normalize-space(text()) = '10']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[14]/td[normalize-space(text()) = '8']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[15]/td[normalize-space(text()) = '2']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[16]/td[normalize-space(text()) = '1']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[17]/td[normalize-space(text()) = '9']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[18]/td[normalize-space(text()) = '360.50']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[19]/td[normalize-space(text()) = '835.20']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[20]/td[normalize-space(text()) = '33.03']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[21]/td[normalize-space(text()) = '2,866.23']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[22]/td[normalize-space(text()) = '2.25']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[23]/td[normalize-space(text()) = '1.21']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[24]/td[normalize-space(text()) = '1,128.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[25]/td[normalize-space(text()) = '11,424.00']"));
	
		this.signOut();
	
	}
	
	@Test
	@Order(10)
	void showNegativeCase() {
		 super.signIn("administrator", "administrator"); 
		 super.clickOnMenu("Administrator", "Dashboard");			 
	 
	     String path=super.getSimplePath(); 
	     String query=super.getContextQuery(); 
	 
	     super.signOut();  
	     super.navigate(path,query); 
	     super.checkErrorsExist(); 
	    } 
}
