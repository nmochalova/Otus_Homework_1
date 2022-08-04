package exeptions;

public class DriverTypeNotSupported extends Exception {

    public DriverTypeNotSupported(String browser) {
        super(String.format("Browser type %s doesn't support", browser));
    }
}
