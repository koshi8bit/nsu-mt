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

    private void fill(String name, String value)
    {
        browser.waitForElement(By.name(name));
        browser.typeText(By.name(name), value);
    }

    public CreateCustomerScreen fillEmail(String email) {
        fill("login", email);
        return this;
    }

    public CreateCustomerScreen fillPassword(String password) {
        fill("password", password);
        return this;
    }

    public CreateCustomerScreen fillFirstName(String firstName) {
        fill("firstName", firstName);
        return this;
    }

    public CreateCustomerScreen fillLastName(String lastName) {
        fill("lastName", lastName);
        return this;
    }

    // OK Лабораторная 4: Подумайте как обработать ситуацию,
    // когда при нажатии на кнопку Submit ('Create') не произойдет переход на AdminScreen,
    // а будет показана та или иная ошибка на текущем скрине.
    public AdminScreen clickSubmit() throws exxx {
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        Assert.assertEquals(browser.currentPage(), "http://localhost:8080/tm-frontend/add-customer");
        browser.click(By.xpath("//button[@type = 'submit']"));
        try
        {
            browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"), 2);
            return new AdminScreen(browser);
        }
        catch (TimeoutException e)
        {
            Assert.assertEquals(browser.currentPage(),
                    "http://localhost:8080/tm-frontend/add-customer");
            String message = browser.getText(By.xpath("/html/body/div[1]/div/div/div[1]"));
            throw new exxx(message);
        }
    }

    public AdminScreen clickCancel() {
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        browser.click(By.xpath("//button[contains(text(),'Back')]"));
        browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
        return new AdminScreen(browser);
    }
}
