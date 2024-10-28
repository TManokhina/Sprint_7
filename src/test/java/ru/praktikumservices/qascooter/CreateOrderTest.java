package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertNotNull;
import static ru.praktikumservices.qascooter.RequestUtils.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    Integer track;

    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getNumberFaq() {
        return new Object[][]{
                {new String[]{"BLACK", "GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{}},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI_PATH;
    }

    @Test
    @Description("Проверка возможности создания заказа - тело ответа должно содержать track")
    public void checkOrderCreation() {
        Order order = new Order("Might", "Guy", "Konoha, 45 apt.", "Metro5", "+7 953 355 35 35", 5, "2024-10-31",
                "Lee will pick up the order", color);
        Response orderResponse = createOrder(order);
        track = orderResponse.as(OrderTrack.class).getTrack();
        assertNotNull("Tело ответа не содержит track", track);
    }

    @After
    public void tearDown() {
        given().contentType(JSON).put(CANCEL_ORDER_PATH + track);
    }
}
