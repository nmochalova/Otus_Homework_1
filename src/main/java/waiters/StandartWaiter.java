package waiters;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Набор стандартных ожиданий
 */
public class StandartWaiter implements WaiterInt {
    private WebDriver driver = null;

    public StandartWaiter(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public boolean waitForCondition(ExpectedCondition condition) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, IMPLICITLY_WAIT_SECOND); //явные ожидания, но с интервалом из настроек для неявных
        try {
            webDriverWait.until(condition);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean elementShouldBePresent(By locator) {
        return this.waitForCondition(ExpectedConditions.presenceOfElementLocated(locator));
    }
}
