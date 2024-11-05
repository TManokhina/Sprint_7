package ru.praktikumservices.qascooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikumservices.qascooter.Courier;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static ru.praktikumservices.qascooter.CourierCreds.credsFromCourier;

public class CourierClient implements Client{
    public static final String LOGIN_PATH = "/api/v1/courier/login";
    public static final String COURIER_PATH = "/api/v1/courier";

    @Step("Send POST request to create courier.")
    public static Response createCourier(Courier courier) {
        return given()
                .contentType(JSON)
                .and()
                .body(courier)
                .post(COURIER_PATH);
    }

    @Step("Send POST request to login courier.")
    public static Response login(Courier courier) {
        return given()
                .contentType(JSON)
                .and()
                .body(credsFromCourier(courier)).post(LOGIN_PATH);
    }
}
