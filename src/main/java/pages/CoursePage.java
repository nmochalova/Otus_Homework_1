package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoursePage extends Page{

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public String getTitleByCourse(String titleBeforeClick) {
        By locator;

        //Локаторы для разных курсов различаются
        if (!titleBeforeClick.contains("Специализация"))
             locator = By.cssSelector(".course-header2__title");
         else
             locator = By.tagName("title");

       //Ожидаем появления заголовка на странице
        assertTrue(standartWaiter.elementShouldBePresent(locator));

       return driver.findElement(locator).getAttribute("innerText");
    }

}
