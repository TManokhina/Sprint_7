package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.*;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertNotNull;
import static ru.praktikumservices.qascooter.RequestUtils.*;

public class CourierLoginTest {
    static String login;
    static String password;
    static Courier courier;
    static Integer id;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URI_PATH;
        login = randomAlphabetic(7);
        password = randomAlphabetic(13);
        courier = new Courier().withLogin(login).withPassword(password);
        createCourier(courier);
    }

    @Test
    @Description("Проверка отсутствия возможности аутентификации под несуществующим пользователем - запрос должен вернуть 404.")
    public void checkLoginWithFictionCreds() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(7));
        courier.withPassword(randomAlphabetic(13));

        Response loginCourierResponse = login(courier);
        assertEquals("Неверный статус код при аутентификации с несуществующей парой логин-пароль", HTTP_NOT_FOUND,
                loginCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности аутентификации без указания логина - запрос должен вернуть 400.")
    public void checkLoginWithoitLoginField() {
        Courier courier = new Courier();
        courier.withLogin("");
        courier.withPassword(password);
        Response loginCourierResponse = RequestUtils.login(courier);
        assertEquals("Неверный статус код при аутентификации без логина", HTTP_BAD_REQUEST,
                loginCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности аутентификации без указания пароля - запрос должен вернуть 400.")
    public void checkLoginWithoutPasswordField() {
        Courier courier = new Courier();
        courier.withLogin(login);
        courier.withPassword("");
        Response loginCourierResponse = RequestUtils.login(courier);
        assertEquals("Неверный статус код при аутентификации без логина", HTTP_BAD_REQUEST,
                loginCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности аутентификации при неправильном указании логина - запрос должен вернуть 404.")
    public void checkLoginWithoutIncorrectLogin() {
        Courier courier = new Courier();
        courier.withLogin(randomAlphabetic(8));
        courier.withPassword(password);
        Response loginCourierResponse = RequestUtils.login(courier);
        assertEquals("Неверный статус код при аутентификации с некорректным логином", HTTP_NOT_FOUND,
                loginCourierResponse.statusCode());
    }

    @Test
    @Description("Проверка отсутствия возможности аутентификации при неправильном указании пароля - запрос должен вернуть 404.")
    public void checkLoginWithoutIncorrectPassword() {
        Courier courier = new Courier();
        courier.withLogin(login);
        courier.withPassword(randomAlphabetic(14));
        Response loginCourierResponse = RequestUtils.login(courier);
        assertEquals("Неверный статус код при аутентификации с некорректным паролем", HTTP_NOT_FOUND,
                loginCourierResponse.statusCode());

    }

    @Test
    @Description("Проверка, что успешный логин возвращает Id")
    public void checkSuccessfulLoginReturnsId() {
        Response loginCourierResponse = RequestUtils.login(courier);
        assertEquals("Неверный статус код при аутентификации", HTTP_OK, loginCourierResponse.statusCode());
        id = RequestUtils.login(courier).as(CourierId.class).getId();
        assertNotNull("Логин не вернул Id", id);
    }

    @AfterClass
    public static void tearDown() {
        given().contentType(JSON).delete(COURIER_PATH + "/" + id);
    }
}
