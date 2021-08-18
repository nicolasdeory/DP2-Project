package acme.testing.anonymous.huston;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import acme.testing.AcmeTest;

class AdministratorDashboardHustonShowTest extends AcmeTest {
	
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


	@Test
	@Order(20)
	void show() {
		this.signIn("administrator", "administrator");
		super.clickOnMenu("Administrator", "Dashboard");		
		
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[1]/td[normalize-space(text()) = '4.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[2]/td[normalize-space(text()) = '1.00']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[3]/td[normalize-space(text()) = '12.62']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[4]/td[normalize-space(text()) = '7.50']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[5]/td[normalize-space(text()) = '6.51']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[6]/td[normalize-space(text()) = '0.39']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[7]/td[normalize-space(text()) = '15.71']"));
		super.checkExists(By.xpath("/html/body/div[2]/div/table/tbody/tr[8]/td[normalize-space(text()) = '17.42']"));
		this.signOut();
	
	}

	


	@Test
	@Order(10)
	void showNegativeCase() {
		 super.signIn("administrator", "administrator"); 
		 super.clickOnMenu("Administrator", "Dashboard");

		String url=super.driver.getCurrentUrl();
	 
	     super.signOut();  
	     super.driver.navigate().to(url);
	     super.checkErrorsExist(); 
	    }

}
