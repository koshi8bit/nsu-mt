package org.nsu.fit.tests.ui;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.tests.ui.screen.CustomerScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

import java.util.UUID;

public class CustomerTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Add some cash")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer")
    public void addSomeCash() throws exxx {
        new LoginScreen(browser)
                .loginAsCustomer("a@b.com", "1234567")
                .addSomeCash()
                .fillCash(100)
                .clickSubmit();
    }

    @Test(description = "Buy plan", dependsOnMethods = "addSomeCash")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer")
    public void buyPlan() throws InterruptedException {
        new CustomerScreen(browser)
                //.loginAsCustomer("a@b.com", "1234567")
                .buyFirstPlan();
    }

    @Test(description = "Delete plan", dependsOnMethods = "buyPlan")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer")
    public void deletePlan() throws InterruptedException {
        new CustomerScreen(browser)
                //.loginAsCustomer("a@b.com", "1234567")
                .deleteLastPlan();
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
