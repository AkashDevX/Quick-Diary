package com.kanbanexam.studyflow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UiUtils {
    private static final String PREFS_NAME = "studyflow_prefs";
    private static final String KEY_THEME = "theme_mode";
    private static final String KEY_DEFAULT_FOCUS = "default_focus_minutes";
    private static final String KEY_DEFAULT_REVISION = "default_revision_minutes";
    
    public static String getGreeting() {
        int hour = new Date().getHours();
        if (hour < 12) return "Good Morning";
        if (hour < 17) return "Good Afternoon";
        return "Good Evening";
    }
    
    public static int getThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME, 0); // 0=Light, 1=Dark, 2=System
    }
    
    public static void setThemeMode(Context context, int mode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME, mode).apply();
    }
    
    public static int getDefaultFocusMinutes(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_DEFAULT_FOCUS, 25);
    }
    
    public static void setDefaultFocusMinutes(Context context, int minutes) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_DEFAULT_FOCUS, minutes).apply();
    }
    
    public static int getDefaultRevisionMinutes(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_DEFAULT_REVISION, 15);
    }
    
    public static void setDefaultRevisionMinutes(Context context, int minutes) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_DEFAULT_REVISION, minutes).apply();
    }
    
    public static String formatDate(long timestamp) {
        return new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date(timestamp));
    }
    
    public static String formatDateTime(long timestamp) {
        return new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(new Date(timestamp));
    }
    
    public static int daysBetween(long start, long end) {
        return (int) ((end - start) / (24 * 60 * 60 * 1000L));
    }
}

