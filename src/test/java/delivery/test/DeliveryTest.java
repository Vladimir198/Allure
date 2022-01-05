package delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import delivery.data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static delivery.data.DataGenerator.deleteString;
import static java.time.Duration.ofSeconds;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");


        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replant meeting")
    void shouldSuccessfulPlanAndReplantMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder='Город']").setValue(validUser.getCity());
        $("[placeholder='Дата встречи']").setValue(deleteString()).setValue(firstMeetingDate);
        $("[name='name']").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();

        $$(".icon_name_close").get(0).click();
        $("[placeholder='Дата встречи']").setValue(deleteString()).setValue(secondMeetingDate);
        $$("button").find(exactText("Запланировать")).click();
        $$("button").find(exactText("Перепланировать")).click();
        $(".notification__content").should(visible, ofSeconds(15)).shouldBe(
                exactTextCaseSensitive("Встреча успешно запланирована на "
                + secondMeetingDate));
    }
}

