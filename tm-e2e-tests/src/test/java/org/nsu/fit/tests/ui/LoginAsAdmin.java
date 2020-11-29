package org.nsu.fit.tests.ui;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

public class CreateCustomerTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create customer via UI")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() throws exxx {
        CustomerPojo customerPojo = new CustomerPojo();
        Faker faker = new Faker();

        customerPojo.firstName = faker.name().firstName();
        customerPojo.lastName = faker.name().lastName();
        customerPojo.login = faker.internet().emailAddress();
        customerPojo.pass = faker.internet().password(6, 12);

        new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customerPojo.login)
                .fillPassword(customerPojo.pass)
                .fillFirstName(customerPojo.firstName)
                .fillLastName(customerPojo.lastName)
                .clickSubmit()
                .isCustomerCreated(customerPojo);

        // OK Лабораторная 4: Проверить что customer создан с ранее переданными полями.
        // Решить проблему с генерацией случайных данных.
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
