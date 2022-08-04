package otus;

import dataTable.DataTableCourse;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.CoursePage;
import pages.MainPage;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

//mvn clean test -Dbrowser="chrome" -Dfilter="QA" -Dtest=FindCourse_Test
public class FindCourse_Test extends BaseTest {

    @Test
    public void FindCourseByName() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openSite();

        List<String> names = mainPage.getNamesAllCourse();

        if (filter == null)
            names.stream().forEach(System.out::println);
        else{
            List<String> namesAfterFilter = mainPage.filterCourseByName(names, filter);
            namesAfterFilter.stream().forEach(System.out::println);
        }
    }

    @Test
    public void getEarlyCourse() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openSite();

        //Набираем в Map плитки всех курсов на странице, отдельно в таблицу парсим заголовок и дату курса.
        HashMap<WebElement, DataTableCourse> nameAndDate = mainPage.getNamesAndDates();

        //Выбираем самый ранний курс
        WebElement course = mainPage.getMinMaxDateOfCourse(nameAndDate, "min");
        String titleBeforeClick = mainPage.getNameOfCourse(course).toUpperCase();

        //наводим курсор на выбранный курс
        Actions actions = new Actions(driver);
        actions.moveToElement(course).build().perform();

        course.click();

        //Переходим на страницу курса
        CoursePage coursePage = new CoursePage(driver);
        String titleAfterClick = coursePage.getTitleByCourse(titleBeforeClick).toUpperCase();

        //Проверяем, что открылась страница в соответствии с выбранным курсом
        assertTrue(titleAfterClick.toUpperCase().contains(titleBeforeClick.toUpperCase()), "TEST_ERROR: The open page does not match the selected course.");
    }

    @Test
    public void getLatestCourse() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openSite();

        HashMap<WebElement, DataTableCourse> nameAndDate = mainPage.getNamesAndDates();

        WebElement course = mainPage.getMinMaxDateOfCourse(nameAndDate, "max");
        String titleBeforeClick = mainPage.getNameOfCourse(course);

        //наводим курсор на выбранный курс
        Actions actions = new Actions(driver);
        actions.moveToElement(course).build().perform();

        course.click();

        CoursePage coursePage = new CoursePage(driver);
        String titleAfterClick = coursePage.getTitleByCourse(titleBeforeClick);

        //Проверяем, что открылась страница в соответствии с выбранным курсом
        assertTrue(titleAfterClick.toUpperCase().contains(titleBeforeClick.toUpperCase()), "TEST_ERROR: The open page does not match the selected course.");
    }

}
