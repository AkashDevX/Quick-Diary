package com.kanbanexam.studyflow.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY startTime DESC")
    List<StudySession> getAll();
    
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY startTime DESC")
    List<StudySession> getBySubjectId(long subjectId);
    
    @Query("SELECT * FROM study_sessions WHERE chapterId = :chapterId ORDER BY startTime DESC")
    List<StudySession> getByChapterId(long chapterId);
    
    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE subjectId = :subjectId")
    Integer getTotalMinutesBySubjectId(long subjectId);
    
    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE startTime >= :startTime AND startTime <= :endTime")
    Integer getTotalMinutesBetween(long startTime, long endTime);
    
    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE subjectId = :subjectId AND startTime >= :startTime AND startTime <= :endTime")
    Integer getTotalMinutesBySubjectBetween(long subjectId, long startTime, long endTime);
    
    @Query("SELECT COUNT(*) FROM study_sessions")
    int getTotalSessions();
    
    @Query("SELECT COUNT(*) FROM study_sessions WHERE subjectId = :subjectId")
    int getSessionCountBySubjectId(long subjectId);
    
    @Insert
    long insert(StudySession session);
}

