package com.eshop.client.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static LocalDate toLocalDate(Date date){
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    public static LocalDate toLocalDate(Long epochMilli){
        return Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public static Long toEpoch(LocalDate date){
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Date toDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static String addZero(int value) {
        if (value < 10)
            return "0" + value;
        else return value + "";
    }
}
