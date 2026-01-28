package com.selfnoteapp.quickdiary.utils;

import android.content.Context;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BackupUtils {
    
    public static String createBackupJson(Context context, List<DiaryEntry> entries) throws JSONException {
        JSONObject backup = new JSONObject();
        backup.put("version", 1);
        backup.put("timestamp", System.currentTimeMillis());
        
        JSONArray entriesArray = new JSONArray();
        for (DiaryEntry entry : entries) {
            JSONObject entryObj = new JSONObject();
            entryObj.put("dateKey", entry.dateKey);
            entryObj.put("text", entry.text);
            entryObj.put("tag", entry.tag != null ? entry.tag : "");
            entryObj.put("mood", entry.mood);
            entryObj.put("moodNote", entry.moodNote != null ? entry.moodNote : "");
            entryObj.put("createdTime", entry.createdTime);
            entryObj.put("lastEditedTime", entry.lastEditedTime);
            entriesArray.put(entryObj);
        }
        backup.put("entries", entriesArray);
        
        backup.put("favoritePrompts", UiUtils.getFavoritePrompts(context));
        
        JSONObject settings = new JSONObject();
        settings.put("themeMode", UiUtils.getThemeMode(context));
        settings.put("fontSize", UiUtils.getFontSize(context));
        settings.put("lockPastEntries", UiUtils.isLockPastEntries(context));
        backup.put("settings", settings);
        
        return backup.toString();
    }
    
    public static void restoreFromJson(Context context, String json) throws JSONException {
        JSONObject backup = new JSONObject(json);
        
        // Restore entries would be handled by the repository
        // This is just parsing logic
        
        JSONArray entriesArray = backup.getJSONArray("entries");
        List<DiaryEntry> entries = new ArrayList<>();
        for (int i = 0; i < entriesArray.length(); i++) {
            JSONObject entryObj = entriesArray.getJSONObject(i);
            DiaryEntry entry = new DiaryEntry();
            entry.dateKey = entryObj.getString("dateKey");
            entry.text = entryObj.getString("text");
            entry.tag = entryObj.optString("tag", null);
            entry.mood = entryObj.optInt("mood", -1);
            entry.moodNote = entryObj.optString("moodNote", null);
            entry.createdTime = entryObj.getLong("createdTime");
            entry.lastEditedTime = entryObj.getLong("lastEditedTime");
            entries.add(entry);
        }
        
        if (backup.has("favoritePrompts")) {
            UiUtils.setFavoritePrompts(context, backup.getString("favoritePrompts"));
        }
        
        if (backup.has("settings")) {
            JSONObject settings = backup.getJSONObject("settings");
            if (settings.has("themeMode")) {
                UiUtils.setThemeMode(context, settings.getInt("themeMode"));
            }
            if (settings.has("fontSize")) {
                UiUtils.setFontSize(context, settings.getInt("fontSize"));
            }
            if (settings.has("lockPastEntries")) {
                UiUtils.setLockPastEntries(context, settings.getBoolean("lockPastEntries"));
            }
        }
    }
}

