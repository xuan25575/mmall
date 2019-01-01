package com.mmall.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //joda time
    public static Date strToDate(String strDate, String dateFormat){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateFormat);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date ,String dateFormat){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateFormat);
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString();
    }

    public static Date strToDate(String strDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString();
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.dateToStr(new Date()));
       // System.out.println(DateUtil.strToDate("2011-10-11 22:22:22"));
    }
}
