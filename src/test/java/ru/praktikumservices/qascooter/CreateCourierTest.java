package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.*;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static ru.praktikumservices.qascooter.RequestUtils.*;

public class CreateCourierTest {

    private final List<Integer> courierIds = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI_PATH;
    }

    @Test
    @Description("Проверка возможности создать курьера при заполнении всех полей: логин, пароль, имя - запрос должен вернуть 200.")
    public void checkCreationCourier() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withPassword(randomAlphabetic(13));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = createCourier(courier);
        Response loginCourierResponse = RequestUtils.login(courier);
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

        Response createCourierResponse = createCourier(courier);
        assertEquals("Неверный статус код при создании курьера без логина", HTTP_BAD_REQUEST,
                createCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности создать курьера без указания пароля - запрос должен вернуть 400.")
    public void checkCreationCourierWithoutPassword() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = createCourier(courier);
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
        createCourier(courier);
        courierIds.add(RequestUtils.login(courier).as(CourierId.class).getId());

        Response createCourierResponse = createCourier(courier);
        courierIds.add(RequestUtils.login(courier).as(CourierId.class).getId());
        assertEquals("Неверный статус код при создании курьера с логином, который уже есть", HTTP_CONFLICT,
                createCourierResponse.statusCode());
    }

    @After
    public void tearDown() {
        for (Integer id : courierIds) {
            given().contentType(JSON).delete(COURIER_PATH + "/" + id);
        }
    }
}
