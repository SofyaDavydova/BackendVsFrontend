package ru.netology.test;

import org.junit.jupiter.api.*;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    @DisplayName("Should successfully login with valid user,valid verification code and transfer " +
            "from first card to second")
    void shouldLoginAndTransferFromFirstCardToSecond() {
        var user = getAuthInfo();
        loginRequest(user);
        var verificationCode = getVerificationCodeFromDataBase();
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        String token = verificationRequest(verificationInfo);
        CardResponseInfo[] cards = cardsView(token);
        CardResponseInfo card1 = cards[0];
        CardResponseInfo card2 = cards[1];
        var balance1 = card1.getBalance();
        var balance2 = card2.getBalance();
        int amount = generateValidAmount(card1.getBalance());
        var expectedBalance1 = balance1 - amount;
        var expectedBalance2 = balance2 + amount;
        String cardNumber1 = getCardNumberByCardId(card1.getId());
        String cardNumber2 = getCardNumberByCardId(card2.getId());
        var transferInfo = getTransferInfo(cardNumber1,
            cardNumber2, amount);
        transferAmount(token, transferInfo);
        cards = cardsView(token);
        card1 = cards[0];
        card2 = cards[1];
        balance1 = card1.getBalance();
        balance2 = card2.getBalance();
        Assertions.assertEquals(expectedBalance1, balance1);
        Assertions.assertEquals(expectedBalance2, balance2);
    }

    @Test
    @DisplayName("Should successfully login with valid user,valid verification code and transfer " +
            "from second card to first")
    void shouldLoginAndTransferFromSecondCardToFirst() {
        var user = getAuthInfo();
        loginRequest(user);
        var verificationCode = getVerificationCodeFromDataBase();
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        String token = verificationRequest(verificationInfo);
        CardResponseInfo[] cards = cardsView(token);
        CardResponseInfo card1 = cards[0];
        CardResponseInfo card2 = cards[1];
        var balance1 = card1.getBalance();
        var balance2 = card2.getBalance();
        int amount = generateValidAmount(card2.getBalance());
        var expectedBalance1 = balance1 + amount;
        var expectedBalance2 = balance2 - amount;
        String cardNumber1 = getCardNumberByCardId(card1.getId());
        String cardNumber2 = getCardNumberByCardId(card2.getId());
        var transferInfo = getTransferInfo(cardNumber2,
                cardNumber1, amount);
        transferAmount(token, transferInfo);
        cards = cardsView(token);
        card1 = cards[0];
        card2 = cards[1];
        balance1 = card1.getBalance();
        balance2 = card2.getBalance();
        Assertions.assertEquals(expectedBalance1, balance1);
        Assertions.assertEquals(expectedBalance2, balance2);
    }

    @Test
    @DisplayName("Should not login with random user")
    void shouldNotLoginWithRandomUser() {
        var user = generateRandomUser();
        String code = loginErrorRequest(user);
        Assertions.assertEquals("AUTH_INVALID", code);
    }

    @Test
    @DisplayName("Should not verify with random verification code")
    void shouldNotVerifyWithRandomVerificationCode() {
        var user = getAuthInfo();
        loginRequest(user);
        var verificationCode = generateRandomVerificationCode();
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        String code = verificationErrorRequest(verificationInfo);
        Assertions.assertEquals("AUTH_INVALID", code);
    }

    @Test
    @DisplayName("Should not transfer when transfer amount more than balance of card from")
    void shouldNotTransferWhenTransferAmountMoreThanBalance() {
        var user = getAuthInfo();
        loginRequest(user);
        var verificationCode = getVerificationCodeFromDataBase();
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        String token = verificationRequest(verificationInfo);
        CardResponseInfo[] cards = cardsView(token);
        CardResponseInfo card1 = cards[0];
        CardResponseInfo card2 = cards[1];
        var balance1 = card1.getBalance();
        var balance2 = card2.getBalance();
        int amount = generateInvalidAmount(card1.getBalance());
        var expectedBalance1 = balance1;
        var expectedBalance2 = balance2;
        String cardNumber1 = getCardNumberByCardId(card1.getId());
        String cardNumber2 = getCardNumberByCardId(card2.getId());
        var transferInfo = getTransferInfo(cardNumber1,
                cardNumber2, amount);
        transferAmount(token, transferInfo);
        cards = cardsView(token);
        card1 = cards[0];
        card2 = cards[1];
        balance1 = card1.getBalance();
        balance2 = card2.getBalance();
        Assertions.assertEquals(expectedBalance1, balance1);
        Assertions.assertEquals(expectedBalance2, balance2);
    }

    @Test
    @DisplayName("Should not transfer when transfer amount is negative")
    void shouldNotTransferWhenNegativeTransferAmount() {
        var user = getAuthInfo();
        loginRequest(user);
        var verificationCode = getVerificationCodeFromDataBase();
        var verificationInfo = getVerificationInfo(user.getLogin(), verificationCode);
        String token = verificationRequest(verificationInfo);
        CardResponseInfo[] cards = cardsView(token);
        CardResponseInfo card1 = cards[0];
        CardResponseInfo card2 = cards[1];
        var balance1 = card1.getBalance();
        var balance2 = card2.getBalance();
        int amount = generateNegativeAmount(balance2);
        var expectedBalance1 = balance1;
        var expectedBalance2 = balance2;
        String cardNumber1 = getCardNumberByCardId(card1.getId());
        String cardNumber2 = getCardNumberByCardId(card2.getId());
        var transferInfo = getTransferInfo(cardNumber2,
                cardNumber1, amount);
        transferAmount(token, transferInfo);
        cards = cardsView(token);
        card1 = cards[0];
        card2 = cards[1];
        balance1 = card1.getBalance();
        balance2 = card2.getBalance();
        Assertions.assertEquals(expectedBalance1, balance1);
        Assertions.assertEquals(expectedBalance2, balance2);
    }

}
