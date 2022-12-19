package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class CoursePage extends Page{

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    @Step("Get title by course")
    public String getTitleByCourse(String titleBeforeClick) {
        By locator = null;

        //Локаторы для разных курсов различаются
        try {
            locator = By.cssSelector(".sc-182qdc9-1");
            return driver.findElement(locator).getText();
        } catch (NoSuchElementException e) {
            locator = By.tagName("title");
            return driver.findElement(locator).getAttribute("innerText");
        } catch (Exception e) {
            throw e;
        }
   }

}
