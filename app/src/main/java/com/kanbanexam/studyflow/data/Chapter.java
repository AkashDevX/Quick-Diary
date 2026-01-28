package com.kanbanexam.studyflow.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "chapters",
    foreignKeys = @ForeignKey(
        entity = Subject.class,
        parentColumns = "id",
        childColumns = "subjectId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("subjectId")}
)
public class Chapter {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public long subjectId;
    public String title;
    public int difficulty; // 0=Easy, 1=Medium, 2=Hard
    public int status; // 0=Not Started, 1=In Progress, 2=Done
    public int revisionLevel; // 0-5
    public long lastStudied; // timestamp
    public long nextRevisionDue; // timestamp
    
    public Chapter() {}
    
    public Chapter(long subjectId, String title, int difficulty, int status) {
        this.subjectId = subjectId;
        this.title = title;
        this.difficulty = difficulty;
        this.status = status;
        this.revisionLevel = 0;
        this.lastStudied = 0;
        this.nextRevisionDue = 0;
    }
}

