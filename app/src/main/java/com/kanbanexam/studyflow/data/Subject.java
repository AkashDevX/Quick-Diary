package com.kanbanexam.studyflow.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class Subject {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String name;
    public String colorHex;
    public int weeklyGoalMinutes;
    
    public Subject() {}
    
    public Subject(String name, String colorHex, int weeklyGoalMinutes) {
        this.name = name;
        this.colorHex = colorHex;
        this.weeklyGoalMinutes = weeklyGoalMinutes;
    }
}

