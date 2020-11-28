package org.nsu.fit.services.rest;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import org.glassfish.jersey.client.ClientConfig;
import org.nsu.fit.services.log.Logger;
import org.nsu.fit.services.rest.data.*;
import org.nsu.fit.shared.JsonMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.UUID;

public class RestClient {
    private static final String REST_URI = "http://localhost:8080/tm-backend/rest";

    private static Client client = ClientBuilder.newClient(new ClientConfig().register(RestClientLogFilter.class));

    public AccountTokenPojo authenticate(String login, String pass) {
        CredentialsPojo credentialsPojo = new CredentialsPojo();

        credentialsPojo.login = login;
        credentialsPojo.pass = pass;

        return post("authenticate", JsonMapper.toJson(credentialsPojo, true), AccountTokenPojo.class, null);
    }

    public CustomerPojo createAutoGeneratedCustomer(AccountTokenPojo accountToken) {
        ContactPojo contactPojo = new ContactPojo();

        Faker faker = new Faker();

        // Лабораторная 3: Добавить генерацию фейковых имен, фамилий и логинов.
        // * Исследовать этот вопрос более детально, возможно прикрутить специальную библиотеку для генерации фейковых данных.
        contactPojo.firstName = faker.name().firstName();
        contactPojo.lastName = faker.name().lastName();
        contactPojo.login = faker.internet().emailAddress();
        contactPojo.pass = faker.internet().password(6, 12);



        return post("customers", JsonMapper.toJson(contactPojo, true), CustomerPojo.class, accountToken);
    }

    public void createAutoGeneratedCustomer(int minPasswordLength, int maxPasswordLength, AccountTokenPojo accountToken) {
        ContactPojo contactPojo = new ContactPojo();

        Faker faker = new Faker();
        contactPojo.firstName = faker.name().firstName();
        contactPojo.lastName = faker.name().lastName();
        contactPojo.login = faker.internet().emailAddress();
        contactPojo.pass = faker.internet().password(minPasswordLength, maxPasswordLength);

        post("customers", JsonMapper.toJson(contactPojo, true), CustomerPojo.class, accountToken);
    }



    public PlanPojo createPlan(int fee, AccountTokenPojo accountToken) {
        PlanPojo planPojo = new PlanPojo();
        Faker faker = new Faker();

        planPojo.id = UUID.randomUUID();
        planPojo.fee = fee;

        Book book = faker.book();
        planPojo.name = book.title();
        //planPojo.details = faker.chuckNorris().fact();
        planPojo.details = book.author();

        return post("plans", JsonMapper.toJson(planPojo, true), PlanPojo.class, accountToken);
    }

    public SubscriptionPojo createSubscription(PlanPojo planPojo, AccountTokenPojo accountToken) {
        SubscriptionPojo subscriptionPojo = new SubscriptionPojo();
        subscriptionPojo.customerId = accountToken.id;
        subscriptionPojo.id = UUID.randomUUID();
        subscriptionPojo.planDetails = planPojo.details;
        subscriptionPojo.planFee = planPojo.fee;
        subscriptionPojo.planName = planPojo.name;
        subscriptionPojo.planId = planPojo.id;
        return post("subscriptions", JsonMapper.toJson(subscriptionPojo, true), SubscriptionPojo.class, accountToken);
    }


    private static <R> R post(String path, String body, Class<R> responseType, AccountTokenPojo accountToken) {
        // Лабораторная 3: Добавить обработку Responses и Errors. Выводите их в лог.
        // Подумайте почему в filter нет Response чтобы можно было удобно его сохранить.
        Invocation.Builder request = client
                .target(REST_URI)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        if (responseType.equals(Response.StatusType.class)) {
            Response.StatusType response;
            try {
                response = request.post(Entity.entity(body, MediaType.APPLICATION_JSON), Response.class).getStatusInfo();
            } catch (Exception e) {
                Logger.debug("Error: " + e.getMessage());
                throw e;
            }
            Logger.debug("Response: " + response.getStatusCode());

            return (R) response;
        } else {
            String response = "";
            try {
                response = request.post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
            } catch (Exception e) {
                Logger.debug("Error: " + e.getMessage());
                throw e;
            }
            Logger.debug("Response: " + response);

            return JsonMapper.fromJson(response, responseType);
        }

    }

    private static class RestClientLogFilter implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) {
            Logger.debug(requestContext.getEntity().toString());

            // Лабораторная 3: разобраться как работает данный фильтр
            // и добавить логирование METHOD и HEADERS.

            Logger.debug(requestContext.getMethod());
            Logger.debug(requestContext.getHeaders().toString());
        }
    }
}
