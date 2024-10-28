package ru.praktikumservices.qascooter;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static ru.praktikumservices.qascooter.CourierCreds.credsFromCourier;

public class RequestUtils {
    public static final String BASE_URI_PATH = "https://qa-scooter.praktikum-services.ru";
    public static final String LOGIN_PATH = "/api/v1/courier/login";
    public static final String COURIER_PATH = "/api/v1/courier";
    public static final String ORDER_PATH = "/api/v1/orders";
    public static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel/";

    @Step("Send POST request to create courier.")
    static Response createCourier(Courier courier) {
        return given()
                .contentType(JSON)
                .and()
                .body(courier)
                .post(COURIER_PATH);
    }

    @Step("Send POST request to login courier.")
    static Response login(Courier courier) {
        return given()
                .contentType(JSON)
                .and()
                .body(credsFromCourier(courier)).post(LOGIN_PATH);
    }

    @Step("Send POST request to create order.")
    static Response createOrder(Order order) {
        return given().contentType(JSON).and().body(order)
                .post(ORDER_PATH);
    }

    @Step("Send GET request to get info about order.")
    static Response getOrder() {
        return given().contentType(JSON).get(ORDER_PATH);
    }

}


