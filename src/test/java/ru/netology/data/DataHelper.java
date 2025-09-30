package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public static void cardsView(String token) {

                        given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .auth().oauth2(token)
                        .when()// "когда"
                        .get("/api/cards") // на какой путь относительно BaseUri отправляем запрос
                        .then()// "тогда ожидаем"
                        .statusCode(200);// код 200 OK

    }

    //@Value
   // public static class CardResponseInfo {
        //String id;
        //String number;
        //int balance;
    //}

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

   // public static String getRandomLogin() {
       // String login = faker.name().username();
       // return login;
   // }

   // public static String getRandomPassword() {
      //  String password = faker.internet().password();
      //  return password;
   // }


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

    public static TransferInfo getTransferInfo() {
        return new TransferInfo("5559 0000 0000 0002", "5559 0000 0000 0008", 5000);
    }


    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }


   // @Data
   // @NoArgsConstructor
   // @AllArgsConstructor
   // public static class VerificationCode {
     //   public String code;
   // }

    public static String getVerificationCode(AuthInfo authInfo) {
        return new String("599640");
    }

  //  public static int generateValidAmount(int balance){
      //  return Math.abs(balance)/10;
  //  }
}