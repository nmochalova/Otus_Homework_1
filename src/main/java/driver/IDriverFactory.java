package driver;

import exeptions.DriverTypeNotSupported;
import org.openqa.selenium.WebDriver;

public interface IDriverFactory {
    public WebDriver getDriver(String browser) throws DriverTypeNotSupported;
}