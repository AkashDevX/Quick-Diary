package com.kanbanexam.studyflow.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ExamSubjectJoinDao {
    @Query("SELECT * FROM exam_subject_join WHERE examId = :examId")
    List<ExamSubjectJoin> getSubjectsByExamId(long examId);
    
    @Query("SELECT * FROM exam_subject_join WHERE subjectId = :subjectId")
    List<ExamSubjectJoin> getExamsBySubjectId(long subjectId);
    
    @Query("SELECT subjectId FROM exam_subject_join WHERE examId = :examId")
    List<Long> getSubjectIdsByExamId(long examId);
    
    @Insert
    void insert(ExamSubjectJoin join);
    
    @Query("DELETE FROM exam_subject_join WHERE examId = :examId")
    void deleteByExamId(long examId);
    
    @Query("DELETE FROM exam_subject_join WHERE examId = :examId AND subjectId = :subjectId")
    void delete(long examId, long subjectId);
}

