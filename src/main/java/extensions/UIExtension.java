package extensions;

import annotaion.Bug;
import annotaion.Driver;


import driver.DriverFactory;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

public class UIExtension implements BeforeEachCallback, AfterEachCallback, ExecutionCondition {

  private WebDriver driver = null;

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
    final Optional<Bug> optional = findAnnotation(extensionContext.getElement(), Bug.class);
    if (optional.isPresent()) {
      return ConditionEvaluationResult.disabled("Bug assigned to test");
    }

    return ConditionEvaluationResult.enabled("Bugs not found");
  }

  private Set<Field> getAnnotatedFields(Class<? extends Annotation> annotation, ExtensionContext extensionContext) {
    Set<Field> set = new HashSet<>();
    Class<?> testClass = extensionContext.getTestClass().get();
    while (testClass != null) {
      for (Field field : testClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(annotation)) {
          set.add(field);
        }
      }
      testClass = testClass.getSuperclass();
    }
    return set;
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    driver = new DriverFactory().getDriver();
    Set<Field> fields = getAnnotatedFields(Driver.class, extensionContext);

    for (Field field : fields) {
      if (field.getType().getName().equals(WebDriver.class.getName())) {
        AccessController.doPrivileged((PrivilegedAction<Void>)
            () -> {
              try {
                field.setAccessible(true);
                field.set(extensionContext.getTestInstance().get(), driver);
              } catch (IllegalAccessException e) {
                throw new Error(String.format("Could not access or set webdriver in field: %s - is this field public?", field), e);
              }
              return null;
            }
        );
      }
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    if (driver != null) {
      if (!System.getProperty("browser","chrome").equals("firefox")) driver.close(); //для firefox close не делаем
      driver.quit();
    }
  }
}
