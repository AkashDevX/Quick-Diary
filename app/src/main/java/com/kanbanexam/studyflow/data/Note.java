package com.kanbanexam.studyflow.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "notes",
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
public class Note {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public long subjectId;
    public Long chapterId; // nullable
    public String text;
    public long createdAt; // timestamp
    public boolean pinned;
    
    public Note() {}
    
    public Note(long subjectId, Long chapterId, String text) {
        this.subjectId = subjectId;
        this.chapterId = chapterId;
        this.text = text;
        this.createdAt = System.currentTimeMillis();
        this.pinned = false;
    }
}

