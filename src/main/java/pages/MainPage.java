package pages;

import dataTable.DataTableCourse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @FindBy(xpath = "//div[@class='container container-lessons']/div[@class='lessons']")
    private WebElement popularCourses;

    @FindBy(xpath = "//div[@class='container-padding-bottom']/div[@class='lessons']")
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
        return course.findElement(By.className("lessons__new-item-title")).getAttribute("innerText");
    }

    public HashMap<WebElement, DataTableCourse> getNamesAndDates() {
        HashMap<WebElement, DataTableCourse> nameAndDate = new HashMap<>();
        String nameCourse, dateCourse;

        List<WebElement> blockPopular = popularCourses.findElements(By.tagName("a"));
        for (WebElement element : blockPopular) {
            nameCourse = element
                    .findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]"))
                    .getText();
            dateCourse = element
                    .findElement(By.xpath(".//div[@class='lessons__new-item-start']"))
                    .getText();
            nameAndDate.put(element, new DataTableCourse(nameCourse, dateCourse));
        }

        List<WebElement> blockSpecial = specializationsCourses.findElements(By.tagName("a"));
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
    //flag принимает значение "max" - для выбора курса, стартующего позже всех и "min" - раньше всех.
    public WebElement getMinMaxDateOfCourse(HashMap<WebElement, DataTableCourse> nameAndDate, String flag) {

        for(Map.Entry<WebElement, DataTableCourse> entry : nameAndDate.entrySet()) {
            Date dt = parserDateRegex(entry.getValue().getDateString());
            if (dt != null) {
                entry.getValue().setDate(dt);
            }
        }

        WebElement result = null;
        if (flag.equals("max")) {
            result = nameAndDate.entrySet().stream()
                    .filter(p -> p.getValue().getDate()!=null)
                    .reduce((s1, s2) -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s1 : s2))
                    .map(p -> p.getKey())
                    .get();
        } else if (flag.equals("min")) {
            result = nameAndDate.entrySet().stream()
                    .filter(p -> p.getValue().getDate()!=null)
                    .reduce((s1,s2) -> (s1.getValue().getDate().before(s2.getValue().getDate()) ? s1 : s2))
                    .map(p -> p.getKey())
                    .get();
        }
        System.out.println("Выбран курс: " + result.getText());
        return result;
    }

    //Парсим строку в массив дат
    private Date parserDateRegex(String StringDateFromSite) {
        int day;
        String month;
        Pattern p = Pattern.compile("(?<day>\\d{1,2})\\W{1,3}(?<month>янв|фев|мар|апр|май|июн|июл|авг|сен|окт|ноя|дек)",
                Pattern.CASE_INSENSITIVE+Pattern.UNICODE_CASE);
        Matcher m = p.matcher(StringDateFromSite);

        if(m.find()) {
            day = Integer.parseInt(m.group("day"));
            month = m.group("month");
            return StringToDate(day, month);
        } else
            return null;
    }

    //Преобразование строки в дату
    private Date StringToDate(int day, String month) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);         //Дописать логику для дат на сломе года. Сейчас ко всем датам подставляется текущий год.

        String monthNumber = getMonth(month);
        try {
            String str = String.format("%d/%s/%d", day, monthNumber, year);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return formatter.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMonth(String month) {
        String monthRUS = String.valueOf(month.toCharArray(), 0, 3);
        switch (monthRUS) {
            case "янв":
                return "01";
            case "фев":
                return "02";
            case "мар":
                return "03";
            case "апр":
                return "04";
            case "май":
                return "05";
            case "июн":
                return "06";
            case "июл":
                return "07";
            case "авг":
                return "08";
            case "сен":
                return "09";
            case "окт":
                return "10";
            case "ноя":
                return "11";
            case "дек":
                return "12";
            default:
                return null;
        }
    }

    public WebElement getCourseByName(String name) {
        String locator = String.format("//div[@class='lessons']//a[contains(@class,'lessons__new-item')][contains(.,'%s')]",name);
        return driver.findElement(By.xpath(locator));
    }

}
