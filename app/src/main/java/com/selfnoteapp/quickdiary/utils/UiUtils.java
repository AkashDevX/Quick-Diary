package com.selfnoteapp.quickdiary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.Calendar;

public class UiUtils {
    private static final String PREFS_NAME = "quick_diary_prefs";
    private static final String KEY_THEME = "theme_mode";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_LOCK_PAST = "lock_past_entries";
    private static final String KEY_PIN_ENABLED = "pin_enabled";
    private static final String KEY_PIN_HASH = "pin_hash";
    private static final String KEY_FAVORITE_PROMPTS = "favorite_prompts";
    
    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static int getThemeMode(Context context) {
        return getPrefs(context).getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
    
    public static void setThemeMode(Context context, int mode) {
        getPrefs(context).edit().putInt(KEY_THEME, mode).apply();
        AppCompatDelegate.setDefaultNightMode(mode);
    }
    
    public static int getFontSize(Context context) {
        return getPrefs(context).getInt(KEY_FONT_SIZE, 1); // 0=small, 1=medium, 2=large
    }
    
    public static void setFontSize(Context context, int size) {
        getPrefs(context).edit().putInt(KEY_FONT_SIZE, size).apply();
    }
    
    public static boolean isLockPastEntries(Context context) {
        return getPrefs(context).getBoolean(KEY_LOCK_PAST, false);
    }
    
    public static void setLockPastEntries(Context context, boolean lock) {
        getPrefs(context).edit().putBoolean(KEY_LOCK_PAST, lock).apply();
    }
    
    public static boolean isPinEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_PIN_ENABLED, false);
    }
    
    public static void setPinEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_PIN_ENABLED, enabled).apply();
    }
    
    public static String getPinHash(Context context) {
        return getPrefs(context).getString(KEY_PIN_HASH, null);
    }
    
    public static void setPinHash(Context context, String hash) {
        getPrefs(context).edit().putString(KEY_PIN_HASH, hash).apply();
    }
    
    public static String getFavoritePrompts(Context context) {
        return getPrefs(context).getString(KEY_FAVORITE_PROMPTS, "[]");
    }
    
    public static void setFavoritePrompts(Context context, String json) {
        getPrefs(context).edit().putString(KEY_FAVORITE_PROMPTS, json).apply();
    }
    
    public static void applyFontSize(TextView textView, Context context) {
        int size = getFontSize(context);
        float scale = 1.0f;
        switch (size) {
            case 0: scale = 0.85f; break; // small
            case 1: scale = 1.0f; break;  // medium
            case 2: scale = 1.2f; break;  // large
        }
        textView.setTextSize(textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity * scale);
    }
    
    public static String getGreeting() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < 12) return "Good Morning";
        if (hour < 17) return "Good Afternoon";
        return "Good Evening";
    }
}

