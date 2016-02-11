package artsam.ua.mycalendar.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
    public static Date addDays(Date date, int daysAmount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, daysAmount); // minus number would decrement the date in daysAmount
        return cal.getTime();
    }

    public static Date addMonth(Date date, int monthsAmount){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, monthsAmount); // minus number would decrement the date in monthsAmount
        return cal.getTime();
    }
}