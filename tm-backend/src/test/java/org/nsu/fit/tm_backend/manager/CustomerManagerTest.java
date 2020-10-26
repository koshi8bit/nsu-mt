package org.nsu.fit.tm_backend.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    private CustomerPojo createCustomerInput;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);

        // Создаем класс, методы которого будем тестировать,
        // и передаем ему наши mock объекты.
        customerManager = new CustomerManager(dbService, logger);
    }

    @Test
    void testCreateCustomer() {
        // настраиваем mock.
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@gmail.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerOutput);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.createCustomer(createCustomerInput);

        // Проверяем результат выполенния метода
        assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).createCustomer(createCustomerInput);

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(1)).getCustomers();
    }

    @Test
    void testCreateCustomerWithNullArgument_Right() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(null));
        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    // Как не надо писать тест...
    @Test
    void testCreateCustomerWithNullArgument_Wrong() {
        try {
            customerManager.createCustomer(null);
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument 'customer' is null.", ex.getMessage());
        }
    }


    /// firstName / lastName {
    @Test
    void testCreateCustomerNameIsNull() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        //createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "ig2i2gd";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.firstName' or 'customer.lastName' is null", exception.getMessage());
    }

    @Test
    void testCreateCustomerNameFirstLowerLetter() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "john";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "ig2i2gd";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("firstName or lastName first letter is not uppercase", exception.getMessage());
    }

    @Test
    void testCreateCustomerNameLenLess02() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "J";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("firstName or lastName length should be more or equal 2 symbols and less or equal 12 symbols", exception.getMessage());
    }

    @Test
    void testCreateCustomerNameLenMore12() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "Jooooooooooooooooooooooooon";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("firstName or lastName length should be more or equal 2 symbols and less or equal 12 symbols", exception.getMessage());
    }

    @Test
    void testCreateCustomerNameSpaceExists() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "J ohn";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("firstName or lastName contains <space>", exception.getMessage());
    }

    @Test
    void testCreateCustomerNameUpperCaseInTheMiddle() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "JoHn";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("firstName or lastName have upper case after first symbol", exception.getMessage());
    }

    @Test
    void testCreateCustomerNameContainsInvalidLetter() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick!";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("firstName or lastName have not valid letters.", exception.getMessage());
    }

    /// firstName / lastName }


    /// login {
    @Test
    void testCreateCustomerLoginIsNull() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        //createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.login' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerLoginIsEmpty() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.login' is empty.", exception.getMessage());
    }

    @Test
    void testCreateCustomerLoginIsInvalid() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john@wick@mail.ru";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Invalid email.", exception.getMessage());
    }

    @Test
    void testCreateCustomerLoginSameLogin() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe7";
        createCustomerInput.balance = 0;


        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick";
        createCustomerOutput1.login = "john_wick@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        CustomerPojo createCustomerOutput2 = new CustomerPojo();
        createCustomerOutput2.id = UUID.randomUUID();
        createCustomerOutput2.firstName = "John2";
        createCustomerOutput2.lastName = "Wick2";
        createCustomerOutput2.login = "john_wick@gmail.com";
        createCustomerOutput2.pass = "Baba_Jaga2";
        createCustomerOutput2.balance = 0;

        LinkedList<CustomerPojo> list = new LinkedList<>();
        list.add(createCustomerOutput1);
        list.add(createCustomerOutput2);

        when(dbService.getCustomers()).thenReturn(list);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Customer with same login is already exists.", exception.getMessage());

        verify(dbService, times(0)).createCustomer(createCustomerInput);
    }
    /// login }

    /// pass {
    @Test
    void testCreateCustomerPasswordIsNull() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        //createCustomerInput.pass = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.pass' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPassLenLess06() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qw";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPassLenMore12() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "1234567890123";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
    }


    @Test
    void testCreateCustomerPasswordIsEasyA() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPasswordIsEasyB() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "1q2w3e";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPasswordContainsInFirstName() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "john12";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password contains in firstName or lastName or login.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPasswordContainsInLastName() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "7wickk";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password contains in firstName or lastName or login.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPasswordContainsInLogin() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "1wick@Gmail2";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password contains in firstName or lastName or login.", exception.getMessage());
    }

    /// pass }

    /// balance {
    @Test
    void testCreateCustomerBalanceIsNotZero() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "wrgwg22qdf";
        createCustomerInput.balance = 3;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Balance is not zero.", exception.getMessage());
    }
    /// balance }


    /// etc {
    @Test
    void testCreateCustomerEtcGetCustomers()
    {
        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick1";
        createCustomerOutput1.login = "john_wick1@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        CustomerPojo createCustomerOutput2 = new CustomerPojo();
        createCustomerOutput2.id = UUID.randomUUID();
        createCustomerOutput2.firstName = "John2";
        createCustomerOutput2.lastName = "Wick2";
        createCustomerOutput2.login = "john_wick2@gmail.com";
        createCustomerOutput2.pass = "Baba_Jaga2";
        createCustomerOutput2.balance = 0;

        LinkedList<CustomerPojo> list = new LinkedList<>();
        list.add(createCustomerOutput1);
        list.add(createCustomerOutput2);

        when(dbService.getCustomers()).thenReturn(list);

        List<CustomerPojo> customers = customerManager.getCustomers();
        assertEquals("Wick1", customers.get(0).lastName);
        assertEquals("Wick2", customers.get(1).lastName);
    }

    @Test
    void testCreateCustomerEtcGetCustomer()
    {
        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick1";
        createCustomerOutput1.login = "john_wick1@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        CustomerPojo createCustomerOutput2 = new CustomerPojo();
        createCustomerOutput2.id = UUID.randomUUID();
        createCustomerOutput2.firstName = "John2";
        createCustomerOutput2.lastName = "Wick2";
        createCustomerOutput2.login = "john_wick2@gmail.com";
        createCustomerOutput2.pass = "Baba_Jaga2";
        createCustomerOutput2.balance = 0;

        LinkedList<CustomerPojo> list = new LinkedList<>();
        list.add(createCustomerOutput1);
        list.add(createCustomerOutput2);

        when(dbService.getCustomers()).thenReturn(list);

        List<CustomerPojo> customers = customerManager.getCustomers();
        assertEquals("Wick1", customers.get(0).lastName);
        assertEquals("Wick2", customers.get(1).lastName);
    }

    @Test
    void testCreateCustomerEtcLookupCustomer()
    {
        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick";
        createCustomerOutput1.login = "john_wick1@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        CustomerPojo createCustomerOutput2 = new CustomerPojo();
        createCustomerOutput2.id = UUID.randomUUID();
        createCustomerOutput2.firstName = "John2";
        createCustomerOutput2.lastName = "Wick2";
        createCustomerOutput2.login = "john_wick2@gmail.com";
        createCustomerOutput2.pass = "Baba_Jaga2";
        createCustomerOutput2.balance = 0;

        LinkedList<CustomerPojo> list = new LinkedList<>();
        list.add(createCustomerOutput1);
        list.add(createCustomerOutput2);

        when(dbService.getCustomers()).thenReturn(list);

        CustomerPojo customer = customerManager.lookupCustomer("john_wick2@gmail.com");
        assertEquals("Wick2", customer.lastName);
    }


    @Test
    void testCreateCustomerEtcToUpBalanceOk()
    {
        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick1";
        createCustomerOutput1.login = "john_wick1@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        when(dbService.getCustomer(createCustomerOutput1.id)).thenReturn(createCustomerOutput1);

        TopUpBalancePojo topUpBalancePojoInput = new TopUpBalancePojo();
        topUpBalancePojoInput.customerId = createCustomerOutput1.id;
        topUpBalancePojoInput.money = 20; // do -20 too

        CustomerPojo customer = customerManager.topUpBalance(topUpBalancePojoInput);

        assertEquals(20, customer.balance);
    }


    @Test
    void testCreateCustomerEtcToUpBalanceMinus20()
    {
        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick1";
        createCustomerOutput1.login = "john_wick1@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        when(dbService.getCustomer(createCustomerOutput1.id)).thenReturn(createCustomerOutput1);

        TopUpBalancePojo topUpBalancePojoInput = new TopUpBalancePojo();
        topUpBalancePojoInput.customerId = createCustomerOutput1.id;
        topUpBalancePojoInput.money = -20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.topUpBalance(topUpBalancePojoInput));
        assertEquals("Addition is less than zero.", exception.getMessage());
    }


    @Test
    void testCreateCustomerEtcToUpBalanceCustomerNotFound()
    {
        CustomerPojo createCustomerOutput1 = new CustomerPojo();
        createCustomerOutput1.id = UUID.randomUUID();
        createCustomerOutput1.firstName = "John";
        createCustomerOutput1.lastName = "Wick1";
        createCustomerOutput1.login = "john_wick1@gmail.com";
        createCustomerOutput1.pass = "Baba_Jaga";
        createCustomerOutput1.balance = 0;

        when(dbService.getCustomer(createCustomerOutput1.id)).thenReturn(createCustomerOutput1);

        TopUpBalancePojo topUpBalancePojoInput = new TopUpBalancePojo();
        topUpBalancePojoInput.customerId = UUID.randomUUID();
        topUpBalancePojoInput.money = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.topUpBalance(topUpBalancePojoInput));
        assertEquals("Customer not found.", exception.getMessage());
    }


    /// etc }
}


