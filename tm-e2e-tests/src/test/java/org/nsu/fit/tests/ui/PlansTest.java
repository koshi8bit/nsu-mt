package org.nsu.fit.tests.ui;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

import java.util.UUID;

public class PlansTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create new plan")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Plans")
    public void createPlan() throws exxx {
        PlanPojo planPojo = new PlanPojo();
        Faker faker = new Faker();

        planPojo.id = UUID.randomUUID();
        planPojo.fee = 20;

        Book book = faker.book();
        planPojo.name = book.title();
        //planPojo.details = faker.chuckNorris().fact();
        planPojo.details = book.author();


        new LoginScreen(browser)
                .loginAsAdmin()
                .createPlan()
                .fillName(planPojo.name)
                .fillDetails(planPojo.details)
                .fillFee(planPojo.fee)
                .clickSubmit()
                .isPlanCreated(planPojo);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
