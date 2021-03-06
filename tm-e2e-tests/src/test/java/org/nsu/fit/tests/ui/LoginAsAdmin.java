package org.nsu.fit.tests.ui;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

public class LoginAsAdmin {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Login as admin")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login")
    public void authAsAdmin() {
        new LoginScreen(browser)
                .loginAsAdmin();
        Assert.assertEquals(browser.currentPage(), "http://localhost:8080/tm-frontend/admin");
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
