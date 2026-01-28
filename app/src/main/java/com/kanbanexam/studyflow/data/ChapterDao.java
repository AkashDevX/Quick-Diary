package com.kanbanexam.studyflow.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY title ASC")
    List<Chapter> getBySubjectId(long subjectId);
    
    @Query("SELECT * FROM chapters ORDER BY title ASC")
    List<Chapter> getAll();
    
    @Query("SELECT * FROM chapters WHERE id = :id")
    Chapter getById(long id);
    
    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId = :subjectId")
    int getCountBySubjectId(long subjectId);
    
    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId = :subjectId AND status = 2")
    int getCompletedCountBySubjectId(long subjectId);
    
    @Query("SELECT * FROM chapters WHERE nextRevisionDue > 0 AND nextRevisionDue <= :currentTime ORDER BY nextRevisionDue ASC")
    List<Chapter> getDueRevisions(long currentTime);
    
    @Query("SELECT * FROM chapters WHERE nextRevisionDue > :currentTime ORDER BY nextRevisionDue ASC")
    List<Chapter> getUpcomingRevisions(long currentTime);
    
    @Insert
    long insert(Chapter chapter);
    
    @Update
    void update(Chapter chapter);
    
    @Delete
    void delete(Chapter chapter);
    
    @Query("DELETE FROM chapters WHERE id = :id")
    void deleteById(long id);
}

