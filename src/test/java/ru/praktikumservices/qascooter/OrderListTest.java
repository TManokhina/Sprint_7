package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static ru.praktikumservices.qascooter.api.client.Client.BASE_URI_PATH;
import static ru.praktikumservices.qascooter.api.client.OrderClient.getOrder;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI_PATH;
    }

    @Test
    @Description("Проверка, что в тело ответа возвращается список заказов.")
    public void checkExistenceOrderListInBody() {
        OrdersInfo ordersInfo = getOrder().body().as(OrdersInfo.class);
        Assert.assertNotNull("В тело ответа не вернулся список заказов.", ordersInfo.getOrders());
    }

}
