package org.nsu.fit.tests.ui;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.log.Logger;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

public class CreateCustomerFailedTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create customer via UI failed")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() throws exxx {
        CustomerPojo customerPojo = new CustomerPojo();
        Faker faker = new Faker();

        customerPojo.firstName = faker.name().firstName();
        customerPojo.lastName = faker.name().lastName();
        customerPojo.login = faker.internet().emailAddress();
        customerPojo.pass = faker.internet().password(1, 3);

        Assert.assertThrows(exxx.class, () ->
                new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customerPojo.login)
                .fillPassword(customerPojo.pass)
                .fillFirstName(customerPojo.firstName)
                .fillLastName(customerPojo.lastName)
                .clickSubmit());

        //browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
//        Assert.assertEquals(browser.currentPage(),
//                "http://localhost:8080/tm-frontend/add-customer");

//        String text = browser.getText(By.xpath("/html/body/div[1]/div/div/div[1]"));
//        Logger.debug(text);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
