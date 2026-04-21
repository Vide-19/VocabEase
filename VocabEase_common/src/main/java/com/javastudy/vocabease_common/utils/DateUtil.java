package com.javastudy.vocabease_common.utils;


import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DateUtil {

    private static final Object lockObj = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date getPreDate(Integer day) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(day);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    // 获取 N天前 的 00:00:00
    public static Date getPreDateStart(Integer day) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(day).withHour(0).withMinute(0).withSecond(0);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    // 获取 N天前 的 23:59:59
    public static Date getPreDateEnd(Integer day) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(day).withHour(23).withMinute(59).withSecond(59);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static List<String> getBetweenDate(Date dateStart, Date dateEnd) {
        LocalDate localDateStart = fromLocalDate2String(dateStart);
        LocalDate localDateEnd = fromLocalDate2String(dateEnd);
        long numOfDays = ChronoUnit.DAYS.between(localDateStart, localDateEnd)+1;
        List<LocalDate> list = Stream.iterate(localDateStart, date ->
                date.plusDays(1)).limit(numOfDays).toList();
        return list.stream().map(date ->
                date.format(DateTimeFormatter.ofPattern(DateTimePatternEnum.YYYY_MM_DD.getPattern()))).toList();
    }

    public static LocalDate fromLocalDate2String(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return instant.atZone(zone).toLocalDate();
    }

}
