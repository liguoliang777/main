package cn.ngame.store.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 时间类型的转换
 */
public class DateUtil {

    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 是否是在当天23点之前
     *
     * @return
     */
    public static boolean isRegisteringTime() {
        Calendar time = Calendar.getInstance();
        if (time.get(Calendar.HOUR_OF_DAY) < 23) {
            return true;
        }
        return false;
    }

    /**
     * 将时间戳转为字符串 ，格式：yyyy.MM.dd
     *
     * @param cc_time
     * @return
     */
    public static String getStrTime_ymd(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        re_StrTime = sdf.format(new Date(cc_time));
        return re_StrTime;
    }

    public static String getStrTime_ymd2(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        re_StrTime = sdf.format(new Date(cc_time));
        return re_StrTime;
    }

    /**
     * 将时间戳转为字符串 ，格式：yyyy年MM月dd日 15:25
     *
     * @param cc_time
     * @return
     */
    public static String getStrTime_ymd_hm(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日 " +
                "HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        re_StrTime = sdf.format(new Date(cc_time));
        return re_StrTime;
    }

    public static String getStrTime_ymd_hm2(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        re_StrTime = sdf.format(new Date(cc_time));
        return re_StrTime;
    }

    public static String getStrTime_ymd_hms(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        re_StrTime = sdf.format(new Date(cc_time));
        return re_StrTime;
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"转为时间戳
     *
     * @param str
     * @return
     */
    public static long dateTimeToTimeStamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String time = String.valueOf(str);
        long newDate = 0;
        try {
            Date date = format.parse(time);
            newDate = date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }


    //格式化yyyy-MM-dd的时间格式
    public static String formatWeek(long time) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }


    //格式化yyyy-MM-dd HH:mm的时间格式
    public static String formatDates(long dateStr) {
        // 2015-09-07T01:58:31.000+0000
        String dateFormat = "";
        if (dateStr == 0) {
            return "";
        } else {
            try {
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(dateStr);
                int month = calendar2.get(Calendar.MONTH);
                int day = calendar2.get(Calendar.DAY_OF_MONTH);

                dateFormat = month + 1 + "月" + day + "日";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateFormat;
    }

    /**
     * 获得指定日期的前一天或者后一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int i) {

        try {
            Calendar c = Calendar.getInstance();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day - i);
            String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            return dayBefore;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * \根据指定时间获取一周的日期
     *
     * @param day    年月日 2015-05-16
     * @param newDay 当前是周几
     * @return
     */
    public static List<String> getListYear(String day, int newDay) {
        List<String> yeare = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            yeare.add(getSpecifiedDayBefore(day, newDay - i));
        }
        return yeare;
    }

    /**
     * java获取两个时间的相隔时间，包括年、月、日、时、分、秒
     *
     * @param beginTime     年月日 2015-05-16
     * @param endTime       年月日 2015-05-16
     * @param formatPattern "yyyy-MM-dd"
     * @param returnPattern 0\1\2\3\4\5
     * @return
     */
    public static long getBetween(String beginTime, String endTime, String formatPattern, int
            returnPattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        Date beginDate = simpleDateFormat.parse(beginTime);
        Date endDate = simpleDateFormat.parse(endTime);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        switch (returnPattern) {
            case 0: //年
                return getByField(beginCalendar, endCalendar, Calendar.YEAR);
            case 1: //月
                return getByField(beginCalendar, endCalendar, Calendar.YEAR) * 12 + getByField
                        (beginCalendar, endCalendar, Calendar.MONTH);
            case 2: //日
                return getTime(beginDate, endDate) / (24 * 60 * 60 * 1000);
            case 3: //时
                return getTime(beginDate, endDate) / (60 * 60 * 1000);
            case 4: //分
                return getTime(beginDate, endDate) / (60 * 1000);
            case 5: //秒
                return getTime(beginDate, endDate) / 1000;
            default:
                return 0;
        }
    }

    private static long getByField(Calendar beginCalendar, Calendar endCalendar, int
            calendarField) {
        return endCalendar.get(calendarField) - beginCalendar.get(calendarField);
    }

    private static long getTime(Date beginDate, Date endDate) {
        return endDate.getTime() - beginDate.getTime();
    }

    /**
     * 获取当天的日期
     *
     * @return
     */
    public static int getTodayDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getMaxDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
