package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataHelper {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static void loginRequest(AuthInfo user) {

        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when().log().all() // "когда"
                .post("/api/auth") // на какой путь относительно BaseUri отправляем запрос
                .then().log().all()// "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String loginErrorRequest(AuthInfo user) {

        String code =
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when().log().all() // "когда"
                .post("/api/auth") // на какой путь относительно BaseUri отправляем запрос
                .then().log().all()// "тогда ожидаем"
                .statusCode(400) // код 200 OK
                .extract()
                .path("code");
        return code;
    }

    public static String verificationRequest(VerificationInfo info) {

        String token =
            given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(info) // передаём в теле объект, который будет преобразован в JSON
                .when().log().all() // "когда"
                .post("/api/auth/verification") // на какой путь относительно BaseUri отправляем запрос
                .then().log().all()// "тогда ожидаем"
                .statusCode(200) // код 200 OK
                .extract()
                .path("token");

        return token;
    }

    public static String verificationErrorRequest(VerificationInfo info) {

        String code =
                given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .body(info) // передаём в теле объект, который будет преобразован в JSON
                        .when().log().all() // "когда"
                        .post("/api/auth/verification") // на какой путь относительно BaseUri отправляем запрос
                        .then().log().all()// "тогда ожидаем"
                        .statusCode(400) // код 200 OK
                        .extract()
                        .path("code");
        return code;
    }

    public static CardResponseInfo[] cardsView(String token) {
        CardResponseInfo[] cards =
                        given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .auth().oauth2(token)
                        .when()// "когда"
                        .get("/api/cards") // на какой путь относительно BaseUri отправляем запрос
                        .then()// "тогда ожидаем"
                        .statusCode(200)// код 200 OK
                        .extract()
                                .response()
                                .as(CardResponseInfo[].class);
        return cards;
    }

    @Value
   public static class CardResponseInfo {
        String id;
        String number;
        int balance;
    }

    public static void transferAmount(String token, TransferInfo transferInfo) {

        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .auth().oauth2(token)
                .body(transferInfo)
                .when()// "когда"
                .post("/api/transfer") // на какой путь относительно BaseUri отправляем запрос
                .then()// "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String generateRandomLogin() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.internet().password();
    }

    public static AuthInfo generateRandomUser (){
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }

    public static String generateRandomVerificationCode(){
        return new String(faker.numerify("######"));
    }


    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationInfo {
        String login;
        String code;
    }

    public static VerificationInfo getVerificationInfo(String login, String code) {
        return new VerificationInfo(login, code);
    }

    @Value
    public static class TransferInfo {
        String from;
        String to;
        int amount;
    }

    public static TransferInfo getTransferInfo(String from, String to, int amount) {
        return new TransferInfo(from, to, amount);
    }


    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static int generateValidAmount(int balance){
        return Math.abs(balance)/10;
    }

    public static int generateInvalidAmount(int balance){
        return Math.abs(balance) + 1;
    }

    public static int generateNegativeAmount(int balance){
        int amount = Math.abs(balance)/10;
        return -amount;
    }
}