package com.example.comp4521_project.helper;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeInterval {
    public static String getInterval(String input) throws ParseException {
        DateFormat df = DateFormat.getInstance();
//        Date date1 = df.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date past = format.parse(input);
//        Date date2 = df.parse(string2);
        assert past != null;
        long difference = new Date().getTime() - past.getTime();

        int years = (int) (difference / (1000 * 60 * 60 * 24 * 30 * 365));
        int months = (int) (difference / (1000 * 60 * 60 * 24 * 30));
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        int sec = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000);

        Log.i("Interval", String.valueOf(difference));
        if (years != 0)
            return years + " y";
        if (months != 0)
            return months + " m";
        if (days != 0)
            return days + " d";
        else if (hours != 0)
            return hours + " h";
        else if (min != 0)
            return min + " min";
        else if (sec != 0)
            return sec + " sec";
        else
            return "error!";

    }
}
