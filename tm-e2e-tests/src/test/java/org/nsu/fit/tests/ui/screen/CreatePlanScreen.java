package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

public class CreatePlanScreen extends Screen {
    public CreatePlanScreen(Browser browser) {
        super(browser);
    }

    private void fill(String name, String value)
    {
        browser.waitForElement(By.name(name));
        browser.removeValue(By.name(name));
        browser.typeText(By.name(name), value);
    }

    public CreatePlanScreen fillName(String name) {
        fill("name", name);
        return this;
    }

    public CreatePlanScreen fillDetails(String details) {
        fill("details", details);
        return this;
    }

    public CreatePlanScreen fillFee(int fee) {
        fill("fee", String.valueOf(fee));
        return this;
    }

    public AdminScreen clickSubmit() throws exxx {
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        browser.click(By.xpath("//button[@type = 'submit']"));
        try
        {
            browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
            return new AdminScreen(browser);
        }
        catch (TimeoutException e)
        {
            Assert.assertEquals(browser.currentPage(),
                    "http://localhost:8080/tm-frontend/add-plan");
            String message = browser.getText(By.xpath("/html/body/div[1]/div/div/div[1]"));
            throw new exxx(message);
        }
    }

}
