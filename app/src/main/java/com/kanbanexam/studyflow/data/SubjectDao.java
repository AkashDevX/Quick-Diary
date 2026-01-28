package com.kanbanexam.studyflow.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM subjects ORDER BY name ASC")
    List<Subject> getAll();
    
    @Query("SELECT * FROM subjects WHERE id = :id")
    Subject getById(long id);
    
    @Insert
    long insert(Subject subject);
    
    @Update
    void update(Subject subject);
    
    @Delete
    void delete(Subject subject);
    
    @Query("DELETE FROM subjects WHERE id = :id")
    void deleteById(long id);
}

