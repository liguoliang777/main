package cn.ngame.store.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

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

    public static String getShowTime(DateTime target, String str) {
        if (target == null) {
            return "";
        }
        DateTime now = getNowServerTime();
        long target_millis = target.getMillis();
        long now_millis = now.getMillis();
        Interval interval = new Interval(target_millis <= now_millis ? target_millis : now_millis, target_millis <= now_millis ? now_millis : target_millis);
        int minute = Minutes.minutesIn(interval).getMinutes();
        int days = Days.daysIn(interval).getDays();
        if (minute < 1) {
            return target.toString("刚刚");
        }
        return target.toString(IsDayAndYesterday(target, str));

//        if (minute > 1) {
//            int hours = Hours.hoursIn(interval).getHours();
//            if (hours < 1) {
//                int minutes = Minutes.minutesIn(interval).getMinutes();
//                if (minutes < 2) {
//                    return "一会儿之前";
//                }
//                return minutes + "分钟之前";
//            }

//            return hours + "小时之前";
//            return target.toString("今天" + "HH:mm");
//        }
//        if (days < 1) {
//            return target.toString("昨天" + "HH:mm");
//        }
//        if (days >= 1 && days < 2) {
//            return target.toString("前天" + "HH:mm");
//        }
//        if (days >= 2) {
//            return target.toString("yyyy-MM-dd HH:mm");
//        }
//        if (days < 7) {
//            return days + "天之前";
//        }
//        if (days < 14) {
//            return "一周之前";
//        }
//        int years = Years.yearsIn(interval).getYears();
//        if (years < 1) {
//            return target.toString("MM-dd");
//        }
//        return target.toString("yyyy-MM-dd");
    }

    public static DateTime getNowServerTime() {
        return new DateTime(System.currentTimeMillis());
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016.06.28 10:10:30" "2016.06.28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static String IsDayAndYesterday(DateTime target, String day) {
        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);

            Calendar cal = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy.MM.dd").parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);
                if (diffDay == 0) {
                    return "今天" + target.toString("HH:mm");
                }
                if (diffDay == -1) {
                    return "昨天" + target.toString("HH:mm");
                }
                if (diffDay == -2) {
                    return "前天" + target.toString("HH:mm");
                }
            } else {
                return target.toString("yyyy-MM-dd HH:mm");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return target.toString("yyyy-MM-dd HH:mm");
    }

//    public static SimpleDateFormat getDateFormat() {
//        if (null == DateLocal.get()) {
//            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
//        }
//        return DateLocal.get();
//    }

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日 " + "HH:mm");
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
    public static String formatDatesToDate(String dateStr) {
        if (dateStr == null || dateStr.length() == 0) {
            return "";
        } else {
            if (dateStr != null && dateStr.contains("T")) {
                dateStr.replace("T", " ");
            }
        }
        String dateFormat = "";
        try {
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dft.parse(dateStr);
            DateFormat dft2 = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat = dft2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormat;
    }


    //格式化yyyy-MM-dd HH:mm的时间格式
    public static String formatDates(String dateStr) {
        // 2015-09-07T01:58:31.000+0000
        String dateFormat = "";
        if (dateStr == null || dateStr.length() == 0) {
            return "";
        } else {
            try {
                if (dateStr.contains("T")) {
                    dateStr = dateStr.replace("T", " ");
                    if (dateStr.contains(".")) {
                        dateStr = dateStr.substring(0, dateStr.indexOf("."));
                    }
                }
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dft.parse(dateStr);
                DateFormat dft2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateFormat = dft2.format(date);
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

    public static String getWeek(int i) {
        String week = "";
        switch (i) {
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
            case 7:
                week = "周日";
                break;
        }
        return week;
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
    public static long getBetween(String beginTime, String endTime, String formatPattern, int returnPattern) throws ParseException {
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
                return getByField(beginCalendar, endCalendar, Calendar.YEAR) * 12 + getByField(beginCalendar, endCalendar, Calendar.MONTH);
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

    private static long getByField(Calendar beginCalendar, Calendar endCalendar, int calendarField) {
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
