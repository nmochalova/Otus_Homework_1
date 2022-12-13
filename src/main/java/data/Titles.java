package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Titles {
  IT_Prof("Выбор профессии в IT", "Выбор профессии в IT"),
  Solution_Architecture("Cloud Solution Architecture","Cloud Solution Architecture"),
  Product_Analytic("Продуктовая аналитика","Продуктовая аналитика"),
  C_Sharp("Специализация С#","Специализация C# Developer"),
  Admin_Linux("Специализация Administrator Linux","Administrator Linux"),
  Admin_Linux_Prof("Administrator Linux. Professional","Administrator Linux. Professional"),
  Sys_Admin("Специализация Системный аналитик","Специализация системный аналитик"),
  Python("Специализация Python","Специализация Python Developer"),
  Machine_Learn("Специализация Machine Learning","Специализация Machine Learning"),
  iOS_Developer("Специализация iOS Developer","Специализация iOS developer"),
  QA_Automation("Специализация QA Automation Engineer","Специализация QA Automation Engineer"),
  Android("Специализация Android","Специализация Android Developer"),
  Java_Developer("Специализация Java-разработчик","Специализация Java Developer"),
  C_plus("Специализация С++","Специализация С++"),
  Fullstack_Dev("Специализация Fullstack Developer","Специализация Fullstack Developer"),
  Network_Engineer("Специализация сетевой инженер", "Специализация Network Engineer"),
  PHP_Developer("Специализация PHP Developer","Специализация PHP Developer"),
  Analitic("Системный аналитик. Advanced","Системный аналитик. Advanced"),
  Data_Engineer("Специализация Data Engineer","Специализация Data Engineer");

  private String titleMainPage;
  private String expectedTitleCoursePage;

  public static String getExpectedTitleCoursePage(String titleBeforeClick) {
    for(Titles title : Titles.values()) {
      if (title.titleMainPage.equals(titleBeforeClick))
        return title.expectedTitleCoursePage;
    }
    return null;
  }
}
