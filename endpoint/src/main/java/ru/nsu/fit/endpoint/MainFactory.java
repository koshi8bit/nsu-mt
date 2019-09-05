package ru.nsu.fit.endpoint;

import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.database.DBService;
import ru.nsu.fit.endpoint.manager.CustomerManager;
import ru.nsu.fit.endpoint.manager.PlanManager;
import ru.nsu.fit.endpoint.manager.SubscriptionManager;

public class MainFactory {
    private static MainFactory instance;

    private CustomerManager customerManager;
    private PlanManager planManager;
    private SubscriptionManager subscriptionManager;

    public MainFactory() {
        DBService dbService = new DBService(LoggerFactory.getLogger("DB_LOG"));

        customerManager = new CustomerManager(dbService, LoggerFactory.getLogger("CUSTOMER_MANAGER_LOG"));
        planManager = new PlanManager(dbService, LoggerFactory.getLogger("PLAN_MANAGER_LOG"));
        subscriptionManager = new SubscriptionManager(dbService, LoggerFactory.getLogger("SUBSCRIPTION_MANAGER_LOG"));
    }

    public static MainFactory getInstance() {
        synchronized (MainFactory.class) {
            if (instance == null) {
                instance = new MainFactory();
            }
            return instance;
        }
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public PlanManager getPlanManager() {
        return planManager;
    }

    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }
}