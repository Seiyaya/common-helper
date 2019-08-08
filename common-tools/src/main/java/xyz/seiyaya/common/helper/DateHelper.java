package xyz.seiyaya.common.helper;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * 日期工具类
 */
public class DateHelper {

    public static final String DATEFORMAT_STR_033 = "yyyy/MM/dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";

    public static final String YYMMDD = "yyMMdd";

    public static final String HHmmss = "HH:mm:ss";

    public static final String yyyyMMddHHmmss="yyyyMMddHHmmss";

    /**
     * 格式化当前时间
     *
     * @param format
     * @return
     */
    public static String formatNowDate(String format) {
        return DateTime.now().toString(format);
    }

    public static String getNDate(Date date,String format,int day){
        return new DateTime(date).plusDays(day).toString(format);
    }

    public static Date getNDate(Date date,int day){
        return new DateTime(date).plusDays(day).toDate();
    }

    public static Date getNDate(int day){
        return new DateTime().plusDays(day).toDate();
    }
}
