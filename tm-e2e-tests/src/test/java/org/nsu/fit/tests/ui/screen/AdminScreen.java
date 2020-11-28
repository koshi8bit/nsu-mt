package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class AdminScreen extends Screen {
    public AdminScreen(Browser browser) {
        super(browser);
    }

    public CreateCustomerScreen createCustomer() {

        browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
        browser.click(By.xpath("//button[@title = 'Add Customer']"));

        return new CreateCustomerScreen(browser);
    }

    public AdminScreen isCustomerCreated(CustomerPojo customerPojo)
    {
        browser.waitForElement(By.xpath("//*[@title='Last Page']"));
        try
        {
            browser.click(By.xpath("//*[@title='Last Page']"), 0);
        }
        catch (Exception ignored) {}

        browser.waitForElement(By.xpath("//*[text() = '" + customerPojo.firstName + "']"));
        browser.waitForElement(By.xpath("//*[text() = '" + customerPojo.lastName + "']"));
        browser.waitForElement(By.xpath("//*[text() = '" + customerPojo.login + "']"));
        return this;
    }
}
