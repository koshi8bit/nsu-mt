package org.nsu.fit.tm_backend.manager;

import com.sun.javafx.scene.web.Debugger;
import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerManager extends ParentManager {
    public CustomerManager(IDBService dbService, Logger flowLog) {
        super(dbService, flowLog);
    }

    /**
     * Метод создает новый объект класса Customer. Ограничения:
     * +Аргумент 'customer' - не null;
     * +firstName - нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов;
     * +lastName  - нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов;
     * login - указывается в виде email, проверить email на корректность, проверить что нет customer с таким же email;
     * +pass - длина от 6 до 12 символов включительно, не должен быть простым (123qwe или 1q2w3e), не должен содержать части login, firstName, lastName
     * +balance - должно быть равно 0 перед отправкой базу данных.
     */
    public CustomerPojo createCustomer(CustomerPojo customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Argument 'customer' is null.");
        }
        //System.out.println("---{");

        isNamesValid(customer.firstName);
        isNamesValid(customer.lastName);

        isLoginValid(customer.login);

        isPassValid(customer);


        if(customer.balance != 0)
        {
            throw new IllegalArgumentException("Balance is not zero.");
        }

        if(lookupCustomer(customer.login) != null)
        {
            throw new IllegalArgumentException("Customer with same login is already exists.");
        }

        //System.out.println("---}");

        // Лабораторная 2: добавить код который бы проверял, что нет customer'а c таким же login (email'ом).
        // Попробовать добавить другие ограничения, посмотреть как быстро растет кодовая база тестов.

        return dbService.createCustomer(customer);
    }

    //+firstName - +нет пробелов, +длина от 2 до 12 символов включительно, +начинается с заглавной буквы, +остальные символы строчные, +нет цифр и других символов;
    private void isNamesValid(String name) //throws IllegalArgumentException
    {
        //System.out.println(name);
        if (name == null) {
            throw new IllegalArgumentException("Field 'customer.firstName' or 'customer.lastName' is null");
        }

        if (name.contains(" "))
        {
            throw new IllegalArgumentException("firstName or lastName contains <space>");
        }

        if (name.length() < 2 || name.length() > 12)
        {
            throw new IllegalArgumentException("firstName or lastName length should be more or equal 2 symbols and less or equal 12 symbols");
        }

        if (!Character.isUpperCase(name.charAt(0)))
        {
            throw new IllegalArgumentException("firstName or lastName first letter is not uppercase");
        }

        String shortString = name.substring(1);

        if (!shortString.equals(shortString.toLowerCase()))
        {
            throw new IllegalArgumentException("firstName or lastName have upper case after first symbol");
        }

        if (!isNameValidLettersOnly(name))
        {
            throw new IllegalArgumentException("firstName or lastName have not valid letters.");
        }
    }

    private boolean isNameValidLettersOnly(String name)
    {
        return name.matches("[a-zA-Z]+");
//        return name.matches ("[[:alpha:]]+");
//
//        String invalidSymbols = "1234567890!@#$";
//        return !name.chars()
//                .mapToObj(x -> ((char) x))
//                .anyMatch(x -> invalidSymbols.contains("" + x));
    }

    //login - +указывается в виде email, +проверить email на корректность, проверить что нет customer с таким же email;
    private void isLoginValid(String login)
    {
        if (login == null) {
            throw new IllegalArgumentException("Field 'customer.login' is null.");
        }

        if (login.isEmpty()) {
            throw new IllegalArgumentException("Field 'customer.login' is empty.");
        }

        if (!login.matches("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$"))
        {
            throw new IllegalArgumentException("Invalid email.");
        }

//        if (lookupCustomer(login) == null)
//        {
//            throw new IllegalArgumentException("User with same login is already exists.");
//        }


    }

    //+pass - +длина от 6 до 12 символов включительно, +не должен быть простым (123qwe или 1q2w3e), +не должен содержать части login, firstName, lastName
    private void isPassValid(CustomerPojo customer)
    {
        String pass = customer.pass;
        if (pass == null) {
            throw new IllegalArgumentException("Field 'customer.pass' is null.");
        }

        if (pass.length() < 6 || pass.length() > 12) {
            throw new IllegalArgumentException("Password's length should be more or equal 6 symbols and less or equal 12 symbols.");
        }

        ArrayList<String> easyPass = new ArrayList<>();
        easyPass.add("123qwe");
        easyPass.add("1q2w3e");
        //easyPass.add("...");

        boolean isSimple = easyPass.stream()
                .anyMatch(x -> x.toLowerCase().contains(pass.toLowerCase()));

        if (isSimple) {
            throw new IllegalArgumentException("Password is very easy.");
        }

        isPassValidContains(pass, customer.login);
        isPassValidContains(pass, customer.firstName);
        isPassValidContains(pass, customer.lastName);


    }

    private void isPassValidContains(String pass, String ect)
    {
        if (pass.toLowerCase().contains(ect.toLowerCase()))
        {
            throw new IllegalArgumentException("Password contains in firstName or lastName or login.");
        }
    }

    /**
     * Метод возвращает список customer'ов.
     */
    public List<CustomerPojo> getCustomers() {
        return dbService.getCustomers();
    }

    public CustomerPojo getCustomer(UUID customerId) {
        return dbService.getCustomer(customerId);
    }

    public CustomerPojo lookupCustomer(String login) {
        return dbService.getCustomers().stream()
                .filter(x -> x.login.equals(login))
                .findFirst()
                .orElse(null);
    }

    public ContactPojo me(AuthenticatedUserDetails authenticatedUserDetails) {
        ContactPojo contactPojo = new ContactPojo();

        if (authenticatedUserDetails.isAdmin()) {
            contactPojo.login = Globals.ADMIN_LOGIN;

            return contactPojo;
        }

        // Лабораторная 2: обратите внимание что вернули данных больше чем надо...
        // т.е. getCustomerByLogin честно возвратит все что есть в базе данных по этому customer'у.
        // необходимо написать такой unit тест, который бы отлавливал данное поведение.
        return dbService.getCustomerByLogin(authenticatedUserDetails.getName());
    }

    public void deleteCustomer(UUID id) {
        dbService.deleteCustomer(id);
    }

    /**
     * Метод добавляет к текущему баласу переданное значение, которое должно быть строго больше нуля.
     */
    public CustomerPojo topUpBalance(TopUpBalancePojo topUpBalancePojo) {
        CustomerPojo customerPojo = dbService.getCustomer(topUpBalancePojo.customerId);

        if (customerPojo == null)
        {
            throw new IllegalArgumentException("Customer not found.");
        }

        if (topUpBalancePojo.money <= 0)
        {
            throw new IllegalArgumentException("Addition is less than zero.");
        }

        customerPojo.balance += topUpBalancePojo.money;

        dbService.editCustomer(customerPojo);

        return customerPojo;
    }
}
