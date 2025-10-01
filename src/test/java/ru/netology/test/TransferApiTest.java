package ru.netology.test;

import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

public class TransferApiTest {


    @AfterEach
    void cleaningAuthCode(){
        cleanAuthCode();
    }

    @Test
    @DisplayName("Should successfully login with valid user,valid verification code and transfer " +
            "from first card to second")
    void shouldLoginFromFirstCardToSecond() {
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
        cardsView(token);
        Assertions.assertEquals(expectedBalance1, balance1);
        Assertions.assertEquals(expectedBalance2, balance2);
    }


}
