package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

public class CustomerScreen extends Screen {
    public CustomerScreen(Browser browser) {
        super(browser);
    }

    private void waitAndClick(String xpath, int index)
    {
        browser.waitForElement(By.xpath(xpath), 1);
        browser.click(By.xpath(xpath), index);
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

    private boolean tryPush(int index)
    {
        String first = "/html/body/div[1]/div/div/div/div/div[1]/div[2]/div/div/div/table/tbody/tr[%]/td[1]/div/button"
                .replace("%", String.valueOf(index));
        String second = first + "[1]";

        if (browser.isElementPresent(By.xpath(first)))
        {
            waitAndClick(first);
            waitAndClick(first, 0);
            return true;
        }
        return false;
    }

    private void pushLastDelete()
    {
        if (tryPush(5))
            return;

        if (tryPush(4))
            return;

        if (tryPush(3))
            return;

        if (tryPush(2))
            return;

        if (tryPush(1))
            return;
    }

    public CustomerScreen deleteLastPlan() throws InterruptedException {
        browser.waitForElement(By.xpath("//*[@title='Last Page']"));
        try
        {
            browser.click(By.xpath("//*[@title='Last Page']"), 0);
        }
        catch (Exception ignored) {}


        pushLastDelete();

        TimeUnit.SECONDS.sleep(2);

        return this;
    }
}
