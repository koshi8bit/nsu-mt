package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class AdminScreen extends Screen {
    public AdminScreen(Browser browser) {
        super(browser);
    }

    private void waitAndClick(String xpath)
    {
        browser.waitForElement(By.xpath(xpath));
        browser.click(By.xpath(xpath));
    }

    public CreateCustomerScreen createCustomer() {

        waitAndClick("//button[@title = 'Add Customer']");

        return new CreateCustomerScreen(browser);
    }

    public CreatePlanScreen createPlan()
    {

        waitAndClick("//button[@title = 'Add plan']");

        return new CreatePlanScreen(browser);
    }

    private void wait(String text)
    {
        browser.waitForElement(By.xpath("//*[text() = '" + text + "']"));
    }

    public AdminScreen isCustomerCreated(CustomerPojo customerPojo)
    {
        browser.waitForElement(By.xpath("//*[@title='Last Page']"));
        try
        {
            browser.click(By.xpath("//*[@title='Last Page']"), 0);
        }
        catch (Exception ignored) {}

        wait(customerPojo.firstName);
        wait(customerPojo.lastName);
        wait(customerPojo.login);

        return this;
    }

    public AdminScreen isPlanCreated(PlanPojo planPojo)
    {
        browser.waitForElement(By.xpath("//*[@title='Last Page']"));
        try
        {
            browser.click(By.xpath("//*[@title='Last Page']"), 1); ///html/body/div[1]/div/div/div/div/div[2]/div[1]/div[4]/div/div/span/button/span[1]/svg/path
        }
        catch (Exception ignored) {}

        wait(planPojo.name);
        wait(planPojo.details);
        wait(String.valueOf(planPojo.fee));

        return this;
    }
}
