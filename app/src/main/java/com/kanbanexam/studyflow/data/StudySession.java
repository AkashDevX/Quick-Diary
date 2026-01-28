package com.kanbanexam.studyflow.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "study_sessions",
    foreignKeys = {
        @ForeignKey(
            entity = Subject.class,
            parentColumns = "id",
            childColumns = "subjectId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Chapter.class,
            parentColumns = "id",
            childColumns = "chapterId",
            onDelete = ForeignKey.SET_NULL
        )
    },
    indices = {@Index("subjectId"), @Index("chapterId")}
)
public class StudySession {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public long subjectId;
    public Long chapterId; // nullable
    public long startTime; // timestamp
    public long endTime; // timestamp
    public int durationMinutes;
    public int type; // 0=Focus, 1=Revision, 2=Practice
    public String note; // nullable
    
    public StudySession() {}
    
    public StudySession(long subjectId, Long chapterId, long startTime, long endTime, int durationMinutes, int type) {
        this.subjectId = subjectId;
        this.chapterId = chapterId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.note = null;
    }
}

