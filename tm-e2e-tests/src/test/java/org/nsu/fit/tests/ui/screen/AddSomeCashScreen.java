package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

public class AddSomeCashScreen extends Screen {
    public AddSomeCashScreen(Browser browser) {
        super(browser);
    }

    private void waitAndClick(String xpath)
    {
        browser.waitForElement(By.xpath(xpath));
        browser.click(By.xpath(xpath));
    }

    private void fill(String name, String value)
    {
        browser.waitForElement(By.name(name));
        browser.removeValue(By.name(name));
        browser.typeText(By.name(name), value);
    }

    public AddSomeCashScreen fillCash(int cash) {
        fill("money", String.valueOf(cash));
        return this;
    }

    private void wait(String text)
    {
        browser.waitForElement(By.xpath("//*[text() = '" + text + "']"));
    }

    public CustomerScreen clickSubmit() throws exxx {
        waitAndClick("//button[@type = 'submit']");
        try
        {
            browser.waitForElement(By.linkText("Top up balance"));
            return new CustomerScreen(browser);
        }
        catch (TimeoutException e)
        {
            Assert.assertEquals(browser.currentPage(),
                    "http://localhost:8080/tm-frontend/top-up-balance");
            String message = browser.getText(By.xpath("/html/body/div[1]/div/div/div[1]"));
            throw new exxx(message);
        }
    }



}

