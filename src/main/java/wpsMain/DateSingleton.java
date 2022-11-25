package wpsMain;

import wpsMain.world.helper.DateHelper;
import org.joda.time.DateTime;

public class DateSingleton {
    private static DateSingleton instance = null;
    private String currentDate = null;

    private DateSingleton(){}

    public String getCurrentDate() {
        return this.currentDate;
    }

    public String getDatePlusOneDayAndUpdate() {
        DateTime date = DateHelper.getDateInJoda(instance.getCurrentDate());
        date = date.plusDays(1);
        String newDate = DateHelper.parseDateTimeToString(date);
        this.setCurrentDate(newDate);
        return newDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public static DateSingleton getInstance() {
        if(instance == null) {
            instance = new DateSingleton();
        }
        return instance;
    }
}
