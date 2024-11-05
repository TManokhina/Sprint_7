package ru.praktikumservices.qascooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikumservices.qascooter.Order;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class OrderClient implements Client{
    public static final String ORDER_PATH = "/api/v1/orders";
    public static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel/";

    @Step("Send POST request to create order.")
    public static Response createOrder(Order order) {
        return given().contentType(JSON).and().body(order)
                .post(ORDER_PATH);
    }

    @Step("Send GET request to get info about order.")
    public static Response getOrder() {
        return given().contentType(JSON).get(ORDER_PATH);
    }
}
