package com.arjun.subjective.demo.utils;

import com.sun.media.sound.SoftTuning;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * Java8中LocalDateTime时间工具类
 */
public class LocalDateTimeUtils {

    //获取当前时间的LocalDateTime对象
    //LocalDateTime.now();

    //根据年月日构建LocalDateTime
    //LocalDateTime.of();

    //比较日期先后
    //LocalDateTime.now().isBefore(),
    //LocalDateTime.now().isAfter(),

    /**
     * Date转换为LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     * @param time
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取指定日期的毫秒
     * @param time
     * @return
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     * @param time
     * @return
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     * @param time
     * @param pattern
     * @return
     */
    public static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间的指定格式
     * @param pattern
     * @return
     */
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime
     * @param endTime
     * @param field     单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     * @param time
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }


    public static void main(String[] args) {
        LocalDateTime local = LocalDateTime.now();

        System.out.println("零点：" + LocalDateTime.of(local.toLocalDate(), LocalTime.MIN));

        System.out.println("末点：" + LocalDateTime.of(local.toLocalDate(), LocalTime.MAX));

        System.out.println("获取默认当前时间为 " + local);

        Clock clock = Clock.systemDefaultZone();
        System.out.println("获取默认当前的Clock " + clock.toString());
        LocalDateTime localDateTime1 = LocalDateTime.now(clock);
        System.out.println("获取Clock时区的时间为" + localDateTime1);

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        System.out.println("localDate是 " + localDate + " localTime是 " + localTime + " 获取的localDateTime2 " + localDateTime2);

        System.out.println("分割线-------------------------------------------------------------------分割线");
        Integer dayOfYear = local.getDayOfYear();
        Integer dayOfMonth = local.getDayOfMonth();
        DayOfWeek dayOfWeek = local.getDayOfWeek();
        System.out.println("获得是年份中的第几天 " + dayOfYear + " 获取月份中的第几天 " + dayOfMonth + " 获得整个星期的星期几 " + dayOfWeek);

        Integer year = local.getYear();
        Month month = local.getMonth();
        Integer hour = local.getHour();
        Integer minute = local.getMinute();
        Integer second = local.getSecond();
        System.out.println("获得年份 " + year + " 获得月份 " + month + " 获得小时 " + hour + " 获得分钟 " + minute + " 获得秒钟 " + second);

        System.out.println("分割线-------------------------------------------------------------------分割线");
        LocalDateTime currentTimeWithDayOfYear = local.withDayOfYear(12);
        LocalDateTime currentTimeWithDayOfMonth = local.withDayOfMonth(11);
        LocalDateTime currentTimeWithYear = local.withYear(2019);
        LocalDateTime currentTimeWithMonth = local.withMonth(1);
        LocalDateTime currentTimeWithHour = local.withHour(12);
        LocalDateTime currentTimeWithMinute = local.withMinute(22);
        LocalDateTime currentTimeWithSecond = local.withSecond(12);
        System.out.println("年份中天变更 " + currentTimeWithDayOfYear + " 月份中天变更 " + currentTimeWithDayOfMonth + " 年份变更 " + currentTimeWithYear
                + "月份变更 " + currentTimeWithMonth + " 时变更 " + currentTimeWithHour + " 分钟变更 " + currentTimeWithMinute
                + "秒变更 " + currentTimeWithSecond);

        System.out.println("分割线-------------------------------------------------------------------分割线");
        LocalDateTime currentTimeAddYear = local.plusYears(1);
        LocalDateTime currentTimeAddMonth = local.plusMonths(1);
        LocalDateTime currentTimeAddDay = local.plusDays(1);
        LocalDateTime currentTimeAddWeek = local.plusWeeks(1);
        LocalDateTime currentTimeAddHour = local.plusHours(1);
        LocalDateTime currentTimeAddSecond = local.plusSeconds(1);
        LocalDateTime currentTimeAddMinutes = local.plusMinutes(1);
        System.out.println("加年份 " + currentTimeAddYear + " 加月份 " + currentTimeAddMonth + " 加天 " + currentTimeAddDay
                + " 加周 " + currentTimeAddWeek + " 加小时 " + currentTimeAddHour + " 加分钟 " + currentTimeAddMinutes
                + " 加秒 " + currentTimeAddSecond);

        LocalDateTime currentTimeMinusYear = local.minusYears(1);
        LocalDateTime currentTimeMinusMonth = local.minusMonths(1);
        LocalDateTime currentTimeMinusWeek = local.minusWeeks(1);
        LocalDateTime currentTimeMinusDay = local.minusDays(1);
        LocalDateTime currentTimeMinusHour = local.minusHours(1);
        LocalDateTime currentTimeMinusMinute = local.minusMinutes(1);
        LocalDateTime currentTimeMinusSecond = local.minusSeconds(1);
        System.out.println("减年份 " + currentTimeMinusYear + " 减月份 " + currentTimeMinusMonth + " 减天 " + currentTimeMinusDay
                + " 减周 " + currentTimeMinusWeek + " 减小时 " + currentTimeMinusHour + " 减分钟 " + currentTimeMinusMinute
                + " 减秒 " + currentTimeMinusSecond);
        System.out.println("分割线-------------------------------------------------------------------分割线");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //它会把时间格式化为指定格式的字符串 创建实例才能使用format()方法
        String nowTime = local.format(formatter);
        System.out.println("格式化后的数据时(得到String类型) " + nowTime);
        //parse()方法通过LocalDateTime
        String str11 = "2007-12-03T10:15:30";
        //注意当String str="2010-1-1 10:10:10"格式会报错
        String str = "2010-01-01 10:10:10";
        LocalDateTime parseTime = LocalDateTime.parse(str11);
        LocalDateTime parseTime1 = LocalDateTime.parse(str, formatter);
        System.out.println("格式化后的数据时(得到LocalDateTime类型) " + parseTime +
                "格式化后的数据时(得到LocalDateTime类型)有俩个参数 " + parseTime1);
        //传入的参数是LocalDateTime类型
        LocalDateTime date = parseTime;
        boolean flag = local.isBefore(date);
        boolean flag1 = local.isAfter(date);
        System.out.println("传入的参数是 " + date + " 是否在传入的数据之前 " + flag + "是否在传入的数据之后 " + flag1);
        //是否相等
        boolean flag2 = local.equals(date);
        System.out.println("localDateTime1和localDateTime2是否相等 " + flag2);
        boolean flag3 = local.isEqual(date);
        System.out.println("localDateTime1和localDateTime2是否相等 " + flag3);

        String Str = date.toString();
        System.out.println("输出字符串 " + Str);
    }
}
