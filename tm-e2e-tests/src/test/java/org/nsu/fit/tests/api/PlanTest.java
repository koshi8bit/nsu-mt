package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.log.Logger;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.InternalServerErrorException;


public class PlanTest {
    private AccountTokenPojo adminToken;
    private RestClient restClient;

    @BeforeClass
    private void auth() {
        restClient = new RestClient();
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Fee = 1")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan")
    public void createFee1Test() {
        PlanPojo planPojo = restClient.createPlan(1, adminToken);
        Assert.assertEquals(planPojo.fee, 1);
        Logger.debug(Integer.toString(planPojo.fee));
    }

    @Test(description = "Fee = 0")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan")
    public void createFee0Test() {
        PlanPojo planPojo = restClient.createPlan(0, adminToken);
        Assert.assertEquals(planPojo.fee, 0);
        Logger.debug(Integer.toString(planPojo.fee));
    }
}
