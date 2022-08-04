package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import waiters.StandartWaiter;

public class Page {
    protected WebDriver driver;
    protected StandartWaiter standartWaiter;        //явные ожидания

    public Page(WebDriver driver) {
        this.driver = driver;
        standartWaiter = new StandartWaiter(driver);
        PageFactory.initElements(driver, this);
    }
}
