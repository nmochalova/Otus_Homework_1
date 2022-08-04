package waiters;

import org.openqa.selenium.support.ui.ExpectedCondition;
/**
 * Интерфейс стандартного набора ожиданий
 * */
public interface WaiterInt {
    boolean waitForCondition(ExpectedCondition condition);

    long IMPLICITLY_WAIT_SECOND = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "5000")) / 1000;
}
