package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.ex.exxx;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

public class CreateCustomerScreen extends Screen {
    public CreateCustomerScreen(Browser browser) {
        super(browser);
    }

    private void aaa(String name, String value)
    {
        browser.waitForElement(By.name(name));
        browser.typeText(By.name(name), value);
    }

    public CreateCustomerScreen fillEmail(String email) {
        aaa("login", email);
        return this;
    }

    public CreateCustomerScreen fillPassword(String password) {
        aaa("password", password);
        return this;
    }

    public CreateCustomerScreen fillFirstName(String firstName) {
        aaa("firstName", firstName);
        return this;
    }

    public CreateCustomerScreen fillLastName(String lastName) {
        aaa("lastName", lastName);
        return this;
    }

    // Лабораторная 4: Подумайте как обработать ситуацию,
    // когда при нажатии на кнопку Submit ('Create') не произойдет переход на AdminScreen,
    // а будет показана та или иная ошибка на текущем скрине.
    public AdminScreen clickSubmit() throws exxx {
        // TODO: Please implement this...
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        Assert.assertEquals(browser.currentPage(), "http://localhost:8080/tm-frontend/add-customer");
        browser.click(By.xpath("//button[@type = 'submit']"));
        try
        {
            browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
            return new AdminScreen(browser);
        }
        catch (TimeoutException e)
        {
            Assert.assertEquals(browser.currentPage(), "http://localhost:8080/tm-frontend/add-customer");
            String message = browser.getText(By.xpath("//div[@id='root'/")); // TODO
            throw new exxx(message);
        }
    }

    public AdminScreen clickCancel() {
        // TODO: Please implement this...
        return new AdminScreen(browser);
    }
}
