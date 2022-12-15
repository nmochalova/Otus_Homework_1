package otus;

import annotaion.Driver;
import data.Titles;
import datatable.DataTableCourse;
import extensions.UIExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.CoursePage;
import pages.MainPage;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

//mvn clean test -Dtest=FindCourse_Test -Dbrowser="chrome" -Dfilter="QA"
//allure serve -h 127.0.0.1 ./allure-results

@Epic("User cases")
@Feature("Checking courses")
@ExtendWith(UIExtension.class)
public class FindCourse_Test {
    @Driver
    public WebDriver driver;

    @Test
    @Story("This test is finding all courses by filter")
    @Description("This test is finding all courses by filter -Dfilter")
    public void findCourseByName() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openSite();

        List<String> names = mainPage.getNamesAllCourse();
        String filter = System.getProperty("filter");
        
        if (filter == null)
            names.forEach(System.out::println);
        else{
            List<String> namesAfterFilter = mainPage.filterCourseByName(names, filter);
            namesAfterFilter.forEach(System.out::println);
        }
    }

    @Test
    @Story("This test is finding earlier course")
    @Description("This test is finding earlier course, than click and check it")
    public void getEarlyCourse() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openSite();

        //Набираем в Map плитки всех курсов на странице, отдельно в таблицу парсим заголовок и дату курса.
        HashMap<WebElement, DataTableCourse> nameAndDate = mainPage.getNamesAndDates();

        //Выбираем самый ранний курс
        WebElement course = mainPage.getMinMaxDateOfCourse(nameAndDate, false); //false = ищем min
        String titleBeforeClick = mainPage.getNameOfCourse(course);

        //наводим курсор на выбранный курс
        mainPage.moveToElement(course);
        mainPage.clickToElement(course);

        //Переходим на страницу курса
        CoursePage coursePage = new CoursePage(driver);
        String titleAfterClick = coursePage.getTitleByCourse(titleBeforeClick);

        //Проверяем, что открылась страница в соответствии с выбранным курсом
        String expectedTitle= Titles.getExpectedTitleCoursePage(titleBeforeClick);

        assertThat(titleAfterClick).isEqualTo(expectedTitle);
    }

    @Test
    @Story("This test is finding latest course")
    @Description("This test is finding latest course, than click and check it")
    public void getLatestCourse() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openSite();

        HashMap<WebElement, DataTableCourse> nameAndDate = mainPage.getNamesAndDates();

        WebElement course = mainPage.getMinMaxDateOfCourse(nameAndDate, true); //true - ищем max
        String titleBeforeClick = mainPage.getNameOfCourse(course);

        //наводим курсор на выбранный курс
        mainPage.clickToElement(course);

        CoursePage coursePage = new CoursePage(driver);
        String titleAfterClick = coursePage.getTitleByCourse(titleBeforeClick);

        //Проверяем, что открылась страница в соответствии с выбранным курсом
        String expectedTitle= Titles.getExpectedTitleCoursePage(titleBeforeClick);

        assertThat(titleAfterClick).isEqualTo(expectedTitle);    }

}
