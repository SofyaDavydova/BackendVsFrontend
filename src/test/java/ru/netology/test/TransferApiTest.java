package ru.netology.test;

import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

public class TransferApiTest {


    //@AfterAll
    //static void cleaningAllTables(){
        //cleanDatabase();
    //}

    @AfterEach
    void cleaningAuthCode(){
        cleanAuthCode();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with valid user and verification code from API description")
    void shouldSuccessfulLoginWithValidUserAndVerificationCodeFromAPIDescription() {
        var user = getAuthInfo();
        var verificationCode = getVerificationCode(user);
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        loginRequest(user);
        String token = verificationRequest(verificationInfo);
        cardsView(token);
        var transferInfo = getTransferInfo();
        transferAmount(token, transferInfo);
    }

    @Test
    @DisplayName("Should successfully login with valid user and verification code from Database")
    void shouldSuccessfulLoginWithValidUserAndVerificationCodeFromDatabase() {
        var user = getAuthInfo();
        loginRequest(user);
        var verificationCode = getVerificationCodeFromDataBase();
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        String token = verificationRequest(verificationInfo);
        cardsView(token);
        var transferInfo = getTransferInfo();
        transferAmount(token, transferInfo);

    }

}
