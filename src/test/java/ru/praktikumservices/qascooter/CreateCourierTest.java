package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.api.client.Client;
import ru.praktikumservices.qascooter.api.client.CourierClient;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.*;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class CreateCourierTest {

    private final List<Integer> courierIds = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = Client.BASE_URI_PATH;
    }

    @Test
    @Description("Проверка возможности создать курьера при заполнении всех полей: логин, пароль, имя - запрос должен вернуть 200.")
    public void checkCreationCourier() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withPassword(randomAlphabetic(13));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = CourierClient.createCourier(courier);
        Response loginCourierResponse = CourierClient.login(courier);
        courierIds.add(loginCourierResponse.as(CourierId.class).getId());

        assertEquals("Неверный статус код при создании курьера", HTTP_CREATED, createCourierResponse.statusCode());
        assertEquals("Неверный статус код при аутентификации", HTTP_OK, loginCourierResponse.statusCode());

    }

    @Test
    @Description("Проверка отсутствия возможности создать курьера без указания логина - запрос должен вернуть 400.")
    public void checkCreationCourierWithoutLogin() {
        Courier courier = new Courier();
        courier.withPassword(randomAlphabetic(13));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = CourierClient.createCourier(courier);
        assertEquals("Неверный статус код при создании курьера без логина", HTTP_BAD_REQUEST,
                createCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности создать курьера без указания пароля - запрос должен вернуть 400.")
    public void checkCreationCourierWithoutPassword() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = CourierClient.createCourier(courier);
        assertEquals("Неверный статус код при создании курьера без пароля", HTTP_BAD_REQUEST,
                createCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности создать пользователя с логином, который уже есть - запрос должен вернуть 409.")
    public void checkCreationIdenticalCouriers() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withPassword(randomAlphabetic(13));
        courier.withFirstName(randomAlphabetic(15));
        CourierClient.createCourier(courier);
        courierIds.add(CourierClient.login(courier).as(CourierId.class).getId());

        Response createCourierResponse = CourierClient.createCourier(courier);
        courierIds.add(CourierClient.login(courier).as(CourierId.class).getId());
        assertEquals("Неверный статус код при создании курьера с логином, который уже есть", HTTP_CONFLICT,
                createCourierResponse.statusCode());
    }

    @After
    public void tearDown() {
        for (Integer id : courierIds) {
            given().contentType(JSON).delete(CourierClient.COURIER_PATH + "/" + id);
        }
    }
}
