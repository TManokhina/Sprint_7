package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static ru.praktikumservices.qascooter.CourierCreds.credsFromCourier;

public class CourierTests {

    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void checkCreationCourier() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withPassword(randomAlphabetic(13));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        assertEquals("Неверный статус код при создании курьера", 201, createCourierResponse.statusCode());

        Response loginCourierResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(credsFromCourier(courier))
                .when()
                .post("/api/v1/courier/login");
        assertEquals("Неверный статус код при аутентификации", 200, loginCourierResponse.statusCode());

        id = loginCourierResponse.as(CourierId.class).getId();
    }

    @Test
    @Description("Если поле логин не заполнено, запрос должен вернуть ошибку 400.")
    public void checkCreationCourierWithoutLogin() {
        Courier courier = new Courier();
        courier.withPassword(randomAlphabetic(13));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        assertEquals("Неверный статус код при создании курьера без логина", 400, createCourierResponse.statusCode());
    }

    @Test
    @Description("Если поле пароль не заполнено, запрос должен вернуть ошибку 400.")
    public void checkCreationCourierWithoutPassword() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withFirstName(randomAlphabetic(15));

        Response createCourierResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        assertEquals("Неверный статус код при создании курьера без пароля", 400, createCourierResponse.statusCode());
    }

    @Test
    public void checkCreationIdenticalCouriers() {
        String login = randomAlphabetic(7);

        Courier courier1 = new Courier();
        courier1.withLogin(login);
        courier1.withPassword(randomAlphabetic(13));
        courier1.withFirstName(randomAlphabetic(15));
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier1)
                .when()
                .post("/api/v1/courier");
        Response loginCourierResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(credsFromCourier(courier1))
                .when()
                .post("/api/v1/courier/login");
        id = loginCourierResponse.as(CourierId.class).getId();

        Courier courier2 = new Courier();
        courier2.withLogin(login);
        courier2.withPassword(randomAlphabetic(13));
        courier2.withFirstName(randomAlphabetic(15));

        Response createCourier2Response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier2)
                .when()
                .post("/api/v1/courier");

        assertEquals("Неверный статус код при создании курьера с логином, который уже есть", 409,
                createCourier2Response.statusCode());

    }

    @After
    public void tearDown() {
        given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + id);

    }
}
