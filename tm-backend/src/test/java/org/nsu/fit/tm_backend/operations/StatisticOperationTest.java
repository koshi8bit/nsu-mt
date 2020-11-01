package org.nsu.fit.tm_backend.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StatisticOperationTest {
    // Лабораторная 2: покрыть юнит тестами класс StatisticOperation на 100%.
    private CustomerManager customerManager;
    private SubscriptionManager subscriptionManager;
    private List<UUID> customerIds = new LinkedList<>();
    private StatisticOperation statisticOperation;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        customerManager = mock(CustomerManager.class);
        subscriptionManager = mock(SubscriptionManager.class);
        //customerIds = mock(List.class);
        for (int i = 0; i < 10; i++) {
            customerIds.add(UUID.randomUUID());
        }

        statisticOperation = new StatisticOperation(customerManager,
                subscriptionManager, customerIds);


    }

    @Test
    void testStatisticOperationCustomerManagerIsNull()
    {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(null, subscriptionManager, customerIds));
        assertEquals("customerManager", exception.getMessage());
    }

    @Test
    void testStatisticOperationSubscriptionManagerIsNull()
    {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, null, customerIds));
        assertEquals("subscriptionManager", exception.getMessage());
    }

    @Test
    void testStatisticOperationCustomerIdsIsNull()
    {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, subscriptionManager, null));
        assertEquals("customerIds", exception.getMessage());
    }

    CustomerPojo createCustomer(UUID id, int balance)
    {
        CustomerPojo createCustomerInput = new CustomerPojo();
        createCustomerInput.id = id;
        createCustomerInput.firstName = "john";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "ig2i2gd";
        createCustomerInput.balance = balance;
        return createCustomerInput;
    }

    SubscriptionPojo createSubs(UUID customer, int fee)
    {
        SubscriptionPojo subs = new SubscriptionPojo();
        subs.customerId = customer;
        subs.planFee = fee;
        subs.planDetails = "some info";
        return subs;
    }

    @Test
    void testExecute()
    {
        int initBalance = 10;
        HashMap<UUID, CustomerPojo> customers = new HashMap<>();
        for (UUID customerId : customerIds)
        {
            CustomerPojo createCustomerInput = createCustomer(customerId, initBalance);
            customers.put(customerId, createCustomerInput);
            when(customerManager.getCustomer(customerId)).thenReturn(customers.get(customerId));
        }

//        HashMap<UUID, SubscriptionPojo> subs = new HashMap<>();
//        for (UUID customerId : customerIds)
//        {
//            SubscriptionPojo createSubInput = new SubscriptionPojo();
//            createSubInput.customerId = customerId;
//            createCustomerInput.firstName = "john";
//            createCustomerInput.lastName = "Wick";
//            createCustomerInput.login = "john_wick@gmail.com";
//            createCustomerInput.pass = "ig2i2gd";
//            createCustomerInput.balance = 10;
//            customers.put(customerId, createCustomerInput);
//            when(customerManager.getCustomer(customerId)).thenReturn(customers.get(customerId));
//        }

        StatisticOperation.StatisticOperationResult res = statisticOperation.Execute();
        assertEquals(initBalance*customers.size(), res.overallBalance);
        //verify(customerManager, times(customerIds.size())).getCustomer();
    }
}
