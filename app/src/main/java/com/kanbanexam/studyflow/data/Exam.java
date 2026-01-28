package com.kanbanexam.studyflow.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exams")
public class Exam {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String name;
    public long examDate; // timestamp
    
    public Exam() {}
    
    public Exam(String name, long examDate) {
        this.name = name;
        this.examDate = examDate;
    }
}

