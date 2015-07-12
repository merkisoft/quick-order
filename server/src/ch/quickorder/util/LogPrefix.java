package ch.quickorder.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPrefix {

    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat( "HH:mm:ss.SSS ");
        }
    };

    public static String currentTime(String text) {
        return dateFormat.get().format(new Date( System.currentTimeMillis())) + text;
    }
}
