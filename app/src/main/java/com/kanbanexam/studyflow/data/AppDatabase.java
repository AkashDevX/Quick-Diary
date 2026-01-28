package com.kanbanexam.studyflow.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
    entities = {
        Subject.class,
        Chapter.class,
        StudySession.class,
        Exam.class,
        ExamSubjectJoin.class,
        Note.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SubjectDao subjectDao();
    public abstract ChapterDao chapterDao();
    public abstract StudySessionDao studySessionDao();
    public abstract ExamDao examDao();
    public abstract ExamSubjectJoinDao examSubjectJoinDao();
    public abstract NoteDao noteDao();
    
    private static volatile AppDatabase INSTANCE;
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "studyflow_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}

