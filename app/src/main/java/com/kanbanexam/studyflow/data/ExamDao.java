package com.kanbanexam.studyflow.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ExamDao {
    @Query("SELECT * FROM exams ORDER BY examDate ASC")
    List<Exam> getAll();
    
    @Query("SELECT * FROM exams WHERE examDate >= :currentTime ORDER BY examDate ASC LIMIT 1")
    Exam getNextExam(long currentTime);
    
    @Query("SELECT * FROM exams WHERE id = :id")
    Exam getById(long id);
    
    @Insert
    long insert(Exam exam);
    
    @Update
    void update(Exam exam);
    
    @Delete
    void delete(Exam exam);
    
    @Query("DELETE FROM exams WHERE id = :id")
    void deleteById(long id);
}

