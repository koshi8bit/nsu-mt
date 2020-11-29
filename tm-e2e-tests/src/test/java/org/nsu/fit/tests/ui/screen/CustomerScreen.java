package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

public class CustomerScreen extends Screen {
    public CustomerScreen(Browser browser) {
        super(browser);
    }

    private void waitAndClick(String xpath)
    {
        browser.waitForElement(By.xpath(xpath), 1);
        browser.click(By.xpath(xpath));
    }

    public AddSomeCashScreen addSomeCash() {

        waitAndClick("/html/body/div[1]/div/div/div/div/p[1]/a");

        return new AddSomeCashScreen(browser);
    }

    public CustomerScreen buyFirstPlan() throws InterruptedException {
        waitAndClick("//*[@title = 'Buy Plan']");
        //waitAndClick("//*[@title = 'Save']"); //TODO NW
        waitAndClick("/html/body/div[1]/div/div/div/div/div[2]/div[2]/div/div/div/table/tbody/tr[1]/td[1]/div/button[1]");
        TimeUnit.SECONDS.sleep(2);

        return this;
    }
}
