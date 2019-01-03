package yokastore.youkagames.com.support.utils.time;

import yokastore.youkagames.com.support.utils.time.model.TimeInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lei Xu on 2018/1/22.
 * 时间工具类
 */

public class TimeUtils {
    public static String getFormatTimebyString(String data){
        String result = null;
        Date dates = TimeUtils.converToDate(data);
        if(isSameDay(dates.getTime())){
            if(isInOneMinute(dates.getTime())){
                result = "刚刚";
            }else if(isInOneHour(dates.getTime())){
                int temp = (int)((System.currentTimeMillis() - dates.getTime())/1000/60);
                if(temp == 0){
                    temp = 1;
                }
                result = temp+"分钟前";
            }else {
                result = "HH:mm";
            }
        }else if(isYesterday(dates.getTime())){
            result = "昨天";
        }else if(isSameYear(dates)){
            result = "MM-dd";
        }else {
            result = "yyyy-MM-dd";
        }
        return (new SimpleDateFormat(result, Locale.CHINA)).format(dates);
    }




    public static Date converToDate(String strDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

   private static boolean isSameYear(Date time){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(time);
       int year = calendar.get(Calendar.YEAR);

       Calendar curCalendar = Calendar.getInstance();
       int curYear = calendar.get(Calendar.YEAR);

       return year == curYear;
    }



    private static boolean isSameDay(long var0) {
        TimeInfo var2 = getTodayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }
    private static boolean isInOneHour(long time){
        TimeInfo into = getOneHourTime();
        return time > into.getStartTime() && time < into.getEndTime();
    }

    private static TimeInfo getOneHourTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long dateStart = calendar.getTime().getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        long dateEnd = calendar2.getTime().getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(dateStart);
        info.setEndTime(dateEnd);
        return info;
    }
    private static boolean isInOneMinute(long time){
        TimeInfo into = getOneMinuteTime();
        return time > into.getStartTime() && time < into.getEndTime();
    }

    private static TimeInfo getOneMinuteTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long dateStart = calendar.getTime().getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        long dateEnd = calendar2.getTime().getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(dateStart);
        info.setEndTime(dateEnd);
        return info;
    }


    public static TimeInfo getTodayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
//        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        Calendar var5 = Calendar.getInstance();
        var5.set(Calendar.HOUR_OF_DAY, 23);
        var5.set(Calendar.MINUTE, 59);
        var5.set(Calendar.SECOND, 59);
        var5.set(Calendar.MILLISECOND, 999);
        Date var6 = var5.getTime();
        long var7 = var6.getTime();
        TimeInfo var9 = new TimeInfo();
        var9.setStartTime(var2);
        var9.setEndTime(var7);
        return var9;
    }

    private static boolean isYesterday(long var0) {
        TimeInfo var2 = getYesterdayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }


    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.add(Calendar.DATE, -1);
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        var4.add(Calendar.DATE, -1);
        var4.set(Calendar.HOUR_OF_DAY, 23);
        var4.set(Calendar.MINUTE, 59);
        var4.set(Calendar.SECOND, 59);
        var4.set(Calendar.MILLISECOND, 999);
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串
     *
     * @param date   日期,给定一个时间
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 取得给定时间,给定格式的日期时间
     *
     * @param time   日期,给定一个时间
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeLong(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }



    /**
     * 取得当前的日期时间字符串YYYY-MM-DD HH:mm:ss
     *
     * @return String 取得当前的日期时间字符串YYYY-MM-DD HH:mm:ss
     */
    public static String getDateTimeString() {
        String format = "yyyy-MM-dd HH:mm:ss";
        return getDateTimeString(format);
    }
    /**
     * 取得当前的日期时间字符串
     *
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTimeString(String format) {
        return toDateTimeString(new Date(), format);
    }


}
