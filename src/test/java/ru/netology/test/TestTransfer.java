package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV1;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;

public class TestTransfer {
    @BeforeEach
    void LoginUser() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneySecondToFirstCard() {
        int value = 100;
        val dashboardPage = new DashboardPage();
        var firstCardBalance = dashboardPage.getFirstCardBalance();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        val dashboardPage2 = dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        val transferPage2 = transferPage.importTransferDataSecondToFirst(value);
        var firstCardBalance1 = dashboardPage2.getFirstCardBalance();
        var secondCardBalance1 = dashboardPage2.getSecondCardBalance();
        Assertions.assertEquals(secondCardBalance - value, secondCardBalance1);
        Assertions.assertEquals(firstCardBalance + value, firstCardBalance1);
    }

    @Test
    void shouldTransferMoneyFirstToSecondCard() {
        int value = 100;
        val dashboardPage = new DashboardPage();
        var firstCardBalance = dashboardPage.getFirstCardBalance();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        val dashboardPage2 = dashboardPage.transferButtonFirstToSecond();
        val transferPage = new TransferPage();
        val transferPage2 = transferPage.importTransferDataFirstToSecond(value);
        var firstCardBalance1 = dashboardPage2.getFirstCardBalance();
        var secondCardBalance1 = dashboardPage2.getSecondCardBalance();
        Assertions.assertEquals(firstCardBalance - value, firstCardBalance1);
        Assertions.assertEquals(secondCardBalance + value, secondCardBalance1);
    }

    @Test
    void doNotShouldTransferMoneyFirstToSecondCardAfterLimit() {
        int value = 100;
        val dashboardPage = new DashboardPage();
        var secondCardBalance = dashboardPage.getSecondCardBalance();
        val dashboardPage2 = dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        val transferPage2 = transferPage.importTransferDataSecondToFirst(value + secondCardBalance);
        transferPage2.getNotification();
    }
}