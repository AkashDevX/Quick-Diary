package com.selfnoteapp.quickdiary.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "diary_entries", indices = {@androidx.room.Index(value = {"dateKey"}, unique = true)})
public class DiaryEntry implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String dateKey; // Format: yyyy-MM-dd, UNIQUE
    public String text;
    public String tag; // nullable
    public int mood; // 0-4: 0=Great, 1=Good, 2=Okay, 3=Bad, 4=Awful
    public String moodNote; // nullable
    public long createdTime;
    public long lastEditedTime;
    
    public DiaryEntry() {
    }
    
    public DiaryEntry(String dateKey, String text, String tag, int mood, String moodNote, long createdTime, long lastEditedTime) {
        this.dateKey = dateKey;
        this.text = text;
        this.tag = tag;
        this.mood = mood;
        this.moodNote = moodNote;
        this.createdTime = createdTime;
        this.lastEditedTime = lastEditedTime;
    }
}

