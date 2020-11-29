package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CustomerScreen extends Screen {
    public CustomerScreen(Browser browser) {
        super(browser);
    }

    private void waitAndClick(String xpath)
    {
        browser.waitForElement(By.xpath(xpath));
        browser.click(By.xpath(xpath));
    }

    public AddSomeCashScreen addSomeCash() {

        waitAndClick("/html/body/div[1]/div/div/div/div/p[1]/a");

        return new AddSomeCashScreen(browser);
    }

    public CustomerScreen buyFirstPlan()
    {

        return this;
    }
}
