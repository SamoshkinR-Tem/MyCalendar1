package artsam.ua.mycalendar.utils;

import java.util.Calendar;
import java.util.Date;

import artsam.ua.mycalendar.MainActivity;

public class CalendarUtil {

    private static final int FIRST_DAY_OF_MONTH = 1;

    private static final int SUNDAY = 6;

    /**
     * Indicating a ViewPager position which is need to initialize an array of objects
     * for grid items.
     */
    private int mPagerPosition;
    private Date[] mDates = new Date[MainActivity.GRID_SIZE];

    public Date obtCurrDate(int position) {
        return  mDates[position];
    }

    public Date[] getDates() {
        return mDates;
    }

    public CalendarUtil(int mPagerPosition) {
        this.mPagerPosition = mPagerPosition;
        initialize();
    }

    private void initialize() {

        Calendar cal = Calendar.getInstance();
        Date firstDayOfMonth = firstOfMonth(cal);
        cal.setTime(firstDayOfMonth);

        // Here we go to target month
        if (mPagerPosition != MainActivity.START_PAGER_POSITION) {
            firstDayOfMonth = DateUtil.addMonth(firstDayOfMonth, mPagerPosition - MainActivity.START_PAGER_POSITION);
            cal.setTime(firstDayOfMonth);
        }

        Date date = cal.getTime();
        // This cycle fills days list from the first day of month include it.
        for (int i = dayOfWeek(cal); i < mDates.length; i++) {
            mDates[i] = cal.getTime();
            date = DateUtil.addDays(date, 1);
            cal.setTime(date);
        }

        cal.setTime(firstDayOfMonth);
        date = firstDayOfMonth;
        // This cycle fills the part of days grid before current day excluding it.
        for (int i = dayOfWeek(cal); i >= 0; i--) {
            mDates[i] = cal.getTime();
            date = DateUtil.addDays(date, -1);
            cal.setTime(date);
        }
    }

    private Date firstOfMonth(Calendar cal) {
        Date date = cal.getTime();
        if (cal.get(Calendar.DAY_OF_MONTH) == FIRST_DAY_OF_MONTH) {
            return date;
        } else {
            date = DateUtil.addDays(date, (FIRST_DAY_OF_MONTH - cal.get(Calendar.DAY_OF_MONTH)));
            return date;
        }
    }

    private int dayOfWeek(Calendar cal) {
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return SUNDAY;
        } else {
            return cal.get(Calendar.DAY_OF_WEEK) - 2;
        }
    }

}
