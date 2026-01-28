package com.selfnoteapp.quickdiary.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface DiaryDao {
    @Query("SELECT * FROM diary_entries WHERE dateKey = :dateKey LIMIT 1")
    DiaryEntry getByDate(String dateKey);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DiaryEntry entry);
    
    @Update
    void update(DiaryEntry entry);
    
    @Query("SELECT * FROM diary_entries ORDER BY dateKey DESC")
    List<DiaryEntry> getAllDesc();
    
    @Query("SELECT * FROM diary_entries WHERE text LIKE '%' || :query || '%' ORDER BY dateKey DESC")
    List<DiaryEntry> search(String query);
    
    @Query("SELECT dateKey FROM diary_entries ORDER BY dateKey DESC")
    List<String> getAllDateKeys();
    
    @Query("DELETE FROM diary_entries")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM diary_entries")
    int getCount();
}

