package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
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
}
