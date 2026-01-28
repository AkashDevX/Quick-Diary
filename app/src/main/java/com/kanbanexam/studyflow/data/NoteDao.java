package com.kanbanexam.studyflow.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY pinned DESC, createdAt DESC")
    List<Note> getAll();
    
    @Query("SELECT * FROM notes WHERE subjectId = :subjectId ORDER BY pinned DESC, createdAt DESC")
    List<Note> getBySubjectId(long subjectId);
    
    @Query("SELECT * FROM notes WHERE chapterId = :chapterId ORDER BY pinned DESC, createdAt DESC")
    List<Note> getByChapterId(long chapterId);
    
    @Query("SELECT * FROM notes WHERE text LIKE '%' || :query || '%' ORDER BY pinned DESC, createdAt DESC")
    List<Note> search(String query);
    
    @Query("SELECT * FROM notes WHERE id = :id")
    Note getById(long id);
    
    @Insert
    long insert(Note note);
    
    @Update
    void update(Note note);
    
    @Delete
    void delete(Note note);
    
    @Query("DELETE FROM notes WHERE id = :id")
    void deleteById(long id);
}

