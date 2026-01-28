package com.kanbanexam.studyflow.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "exam_subject_join",
    foreignKeys = {
        @ForeignKey(
            entity = Exam.class,
            parentColumns = "id",
            childColumns = "examId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Subject.class,
            parentColumns = "id",
            childColumns = "subjectId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("examId"), @Index("subjectId")},
    primaryKeys = {"examId", "subjectId"}
)
public class ExamSubjectJoin {
    public long examId;
    public long subjectId;
    
    public ExamSubjectJoin() {}
    
    public ExamSubjectJoin(long examId, long subjectId) {
        this.examId = examId;
        this.subjectId = subjectId;
    }
}

