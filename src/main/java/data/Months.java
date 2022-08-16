package data;

import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
public enum Months {
    January("янв","01"),
    February("фев","02"),
    March("мар","03"),
    April("апр","04"),
    May("май","05"),
    June("июн","06"),
    July("июл","07"),
    August("авг","08"),
    September("сен","09"),
    October("окт","10"),
    November("ноя","11"),
    December("дек","12");

private String russianSubscription;
private String number;

public static String findMonth(String str) {
    for(Months month : Months.values()) {
        if (month.russianSubscription.equals(str))
            return month.number;
    }
    return null;
}
}
