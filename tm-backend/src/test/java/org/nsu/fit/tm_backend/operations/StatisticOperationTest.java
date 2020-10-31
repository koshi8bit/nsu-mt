package org.nsu.fit.tm_backend.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assertEquals("customerManager1", exception.getMessage());
    }

    @Test
    void testExecute()
    {
        HashMap<UUID, CustomerPojo> myHashMap = new HashMap<>();
        //List<CustomerPojo> customers = new LinkedList<>();
        for (UUID customerId : customerIds)
        {
            CustomerPojo createCustomerInput = new CustomerPojo();
            createCustomerInput.id = customerId;
            createCustomerInput.firstName = "john";
            createCustomerInput.lastName = "Wick";
            createCustomerInput.login = "john_wick@gmail.com";
            createCustomerInput.pass = "ig2i2gd";
            createCustomerInput.balance = 10;
            myHashMap.put(customerId, createCustomerInput);
            when(customerManager.getCustomer(customerId)).thenReturn(myHashMap.get(customerId));
        }
    }
}
