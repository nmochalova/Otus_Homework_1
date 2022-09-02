package annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  //аннотация м.б. использованна во время выполнения программы
@Target(ElementType.FIELD)          //аннотация м.б. использована над полями класса
public @interface Driver {
  String driver() default "";       //параметр driver
}