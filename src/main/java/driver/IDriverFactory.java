package driver;

import exeptions.DriverTypeNotSupported;
import org.openqa.selenium.WebDriver;

public interface IDriverFactory {
    WebDriver getDriver(String browser) throws DriverTypeNotSupported;
}