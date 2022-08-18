package pages;

import data.Months;
import datatable.DataTableCourse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainPage extends Page {
    public MainPage(WebDriver driver) {
        super(driver);
    }
    private final String SITE = "https://otus.ru";
    public void openSite() {
        driver.get(SITE);
    }

    @FindBy(xpath = "//div[@class='container container-lessons']")
    private WebElement popularCourses;

    @FindBy(xpath = "//div[@class='container-padding-bottom']")
    private WebElement specializationsCourses;

    @FindBy(xpath = "//div[@class='lessons']//a[contains(@class,'lessons__new-item')]")
    private List<WebElement> allCourses;

    public List<String> getNamesAllCourse() {
        List<String> names = new ArrayList<>();
        for (WebElement element : allCourses) {
            names.add(element.findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]")).getText());
        }
        return names;
    }

    public String getNameOfCourse(WebElement course) {
        return course.findElement(By.className("lessons__new-item-title")).getText();
    }

    public HashMap<WebElement, DataTableCourse> getNamesAndDates() {
        HashMap<WebElement, DataTableCourse> nameAndDate = new HashMap<>();
        String nameCourse;
        String dateCourse;

        List<WebElement> blockPopular = popularCourses.findElements(By.xpath("./div[@class='lessons']/a"));
        for (WebElement element : blockPopular) {
            nameCourse = element
                    .findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]"))
                    .getText();
            dateCourse = element
                    .findElement(By.xpath(".//div[@class='lessons__new-item-start']"))
                    .getText();
            nameAndDate.put(element, new DataTableCourse(nameCourse, dateCourse));
        }

        List<WebElement> blockSpecial = specializationsCourses.findElements(By.xpath("./div[@class='lessons']/a"));
        for (WebElement element : blockSpecial) {
            nameCourse = element
                    .findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]"))
                    .getText();
            dateCourse = element
                    .findElement(By.xpath(".//div[@class='lessons__new-item-time']"))
                    .getText();
            nameAndDate.put(element, new DataTableCourse(nameCourse, dateCourse));
        }

        return nameAndDate;
    }

    //Метод фильтр по названию курса
    public List<String> filterCourseByName(List<String> names, String name) {
        return names.stream().filter(p -> p.contains(name)).collect(Collectors.toList());
    }

    //Метод выбора курса, стартующего раньше всех/позже всех (при совпадении дат - выбрать любой) при помощи reduce
    //isMax принимает значение "max" - для выбора курса, стартующего позже всех и "min" - раньше всех.
    public WebElement getMinMaxDateOfCourse(HashMap<WebElement, DataTableCourse> nameAndDate, Boolean isMax) {

        for(Map.Entry<WebElement, DataTableCourse> entry : nameAndDate.entrySet()) {
            Date dt = parserDateRegex(entry.getValue().getDateString());
            if (dt != null) {
                entry.getValue().setDate(dt);
            }
        }

        BinaryOperator<Map.Entry<WebElement, DataTableCourse>> binaryOperator = isMax ?
                (Map.Entry<WebElement, DataTableCourse> s1, Map.Entry<WebElement, DataTableCourse> s2)
                        -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s1 : s2):
                (Map.Entry<WebElement, DataTableCourse> s1, Map.Entry<WebElement, DataTableCourse> s2)
                        -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s2 : s1);

        WebElement result = nameAndDate.entrySet().stream()
                .filter(p -> p.getValue().getDate()!=null)
                .reduce(binaryOperator)
                .map(p -> p.getKey())
                .get();

        System.out.println("Выбран курс: " + result.getText());
        return result;
    }

    //Парсим строку в массив дат
    private Date parserDateRegex(String stringDateFromSite) {
        int day;
        String month;
        String year;
        Pattern p = Pattern.compile("(?<day>\\d{1,2})\\W{1,3}(?<month>янв|фев|мар|апр|май|июн|июл|авг|сен|окт|ноя|дек)\\W{1,2}(?<year>\\d{4})?",
                Pattern.CASE_INSENSITIVE+Pattern.UNICODE_CASE);
        Matcher m = p.matcher(stringDateFromSite);

        if(m.find()) {
            day = Integer.parseInt(m.group("day"));
            month = m.group("month");
            year = m.group("year");
            return stringToDate(day, month, year);
        } else
            return null;
    }

    //Преобразование строки в дату
    private Date stringToDate(int day, String month, String year) {
        LocalDate date = LocalDate.now();

        String monthNumber = getMonth(month);
        try {
            String str = String.format("%d/%s/%d", day, monthNumber, year==null ? date.getYear() : Integer.parseInt(year));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return formatter.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMonth(String month) {
        String monthRUS = String.valueOf(month.toCharArray(), 0, 3);

        return Months.findMonth(monthRUS);
    }

    public void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        try {
            actions.moveToElement(element).build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToElement(WebElement element) {
        element.click();
    }

}
