package com.selfnoteapp.quickdiary.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
    private static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);
    
    public static String getTodayDateKey() {
        return DATE_FORMAT.format(new Date());
    }
    
    public static String formatDisplayDate(String dateKey) {
        try {
            Date date = DATE_FORMAT.parse(dateKey);
            return date != null ? DISPLAY_FORMAT.format(date) : dateKey;
        } catch (Exception e) {
            return dateKey;
        }
    }
    
    public static String formatShortDate(String dateKey) {
        try {
            Date date = DATE_FORMAT.parse(dateKey);
            return date != null ? SHORT_FORMAT.format(date) : dateKey;
        } catch (Exception e) {
            return dateKey;
        }
    }
    
    public static String formatTime(long timestamp) {
        return TIME_FORMAT.format(new Date(timestamp));
    }
    
    public static boolean isToday(String dateKey) {
        return dateKey.equals(getTodayDateKey());
    }
    
    public static boolean isPast(String dateKey) {
        try {
            Date date = DATE_FORMAT.parse(dateKey);
            Date today = DATE_FORMAT.parse(getTodayDateKey());
            return date != null && today != null && date.before(today);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String getPreviousDateKey(String dateKey) {
        try {
            Calendar cal = Calendar.getInstance();
            Date date = DATE_FORMAT.parse(dateKey);
            if (date != null) {
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                return DATE_FORMAT.format(cal.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static int calculateStreak(java.util.List<String> dateKeys) {
        if (dateKeys == null || dateKeys.isEmpty()) return 0;
        
        String today = getTodayDateKey();
        int streak = 0;
        String currentDate = today;
        
        while (dateKeys.contains(currentDate)) {
            streak++;
            currentDate = getPreviousDateKey(currentDate);
            if (currentDate == null) break;
        }
        
        return streak;
    }
}

