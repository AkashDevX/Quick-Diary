package com.kanbanexam.studyflow.repo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.kanbanexam.studyflow.data.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyRepository {
    private AppDatabase database;
    private ExecutorService executor;
    private Handler mainHandler;
    
    public StudyRepository(Context context) {
        database = AppDatabase.getDatabase(context);
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    // Subject operations
    public void getAllSubjects(RepositoryCallback<List<Subject>> callback) {
        executor.execute(() -> {
            List<Subject> result = database.subjectDao().getAll();
            mainHandler.post(() -> callback.onResult(result));
        });
    }
    
    public void insertSubject(Subject subject, RepositoryCallback<Long> callback) {
        executor.execute(() -> {
            long id = database.subjectDao().insert(subject);
            mainHandler.post(() -> callback.onResult(id));
        });
    }
    
    public void updateSubject(Subject subject, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.subjectDao().update(subject);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    public void deleteSubject(Subject subject, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.subjectDao().delete(subject);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    // Chapter operations
    public void getChaptersBySubject(long subjectId, RepositoryCallback<List<Chapter>> callback) {
        executor.execute(() -> {
            List<Chapter> result = database.chapterDao().getBySubjectId(subjectId);
            mainHandler.post(() -> callback.onResult(result));
        });
    }
    
    public void insertChapter(Chapter chapter, RepositoryCallback<Long> callback) {
        executor.execute(() -> {
            long id = database.chapterDao().insert(chapter);
            mainHandler.post(() -> callback.onResult(id));
        });
    }
    
    public void updateChapter(Chapter chapter, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.chapterDao().update(chapter);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    public void deleteChapter(Chapter chapter, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.chapterDao().delete(chapter);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    // Study Session operations
    public void insertSession(StudySession session, RepositoryCallback<Long> callback) {
        executor.execute(() -> {
            long id = database.studySessionDao().insert(session);
            mainHandler.post(() -> callback.onResult(id));
        });
    }
    
    public void getWeeklyMinutes(long startTime, long endTime, RepositoryCallback<Integer> callback) {
        executor.execute(() -> {
            Integer result = database.studySessionDao().getTotalMinutesBetween(startTime, endTime);
            mainHandler.post(() -> callback.onResult(result != null ? result : 0));
        });
    }
    
    public void getSubjectMinutes(long subjectId, RepositoryCallback<Integer> callback) {
        executor.execute(() -> {
            Integer result = database.studySessionDao().getTotalMinutesBySubjectId(subjectId);
            mainHandler.post(() -> callback.onResult(result != null ? result : 0));
        });
    }
    
    // Exam operations
    public void getAllExams(RepositoryCallback<List<Exam>> callback) {
        executor.execute(() -> {
            List<Exam> result = database.examDao().getAll();
            mainHandler.post(() -> callback.onResult(result));
        });
    }
    
    public void getNextExam(RepositoryCallback<Exam> callback) {
        executor.execute(() -> {
            Exam result = database.examDao().getNextExam(System.currentTimeMillis());
            mainHandler.post(() -> callback.onResult(result));
        });
    }
    
    public void insertExam(Exam exam, RepositoryCallback<Long> callback) {
        executor.execute(() -> {
            long id = database.examDao().insert(exam);
            mainHandler.post(() -> callback.onResult(id));
        });
    }
    
    public void deleteExam(Exam exam, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.examDao().delete(exam);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    // Revision operations
    public void getDueRevisions(RepositoryCallback<List<Chapter>> callback) {
        executor.execute(() -> {
            long now = System.currentTimeMillis();
            List<Chapter> allChapters = database.chapterDao().getAll();
            List<Chapter> due = new ArrayList<>();
            for (Chapter c : allChapters) {
                if (c.nextRevisionDue > 0 && c.nextRevisionDue <= now) {
                    due.add(c);
                }
            }
            mainHandler.post(() -> callback.onResult(due));
        });
    }
    
    public void getUpcomingRevisions(RepositoryCallback<List<Chapter>> callback) {
        executor.execute(() -> {
            long now = System.currentTimeMillis();
            List<Chapter> allChapters = database.chapterDao().getAll();
            List<Chapter> upcoming = new ArrayList<>();
            for (Chapter c : allChapters) {
                if (c.nextRevisionDue > now) {
                    upcoming.add(c);
                }
            }
            mainHandler.post(() -> callback.onResult(upcoming));
        });
    }
    
    // Note operations
    public void getAllNotes(RepositoryCallback<List<Note>> callback) {
        executor.execute(() -> {
            List<Note> result = database.noteDao().getAll();
            mainHandler.post(() -> callback.onResult(result));
        });
    }
    
    public void searchNotes(String query, RepositoryCallback<List<Note>> callback) {
        executor.execute(() -> {
            List<Note> result = database.noteDao().search(query);
            mainHandler.post(() -> callback.onResult(result));
        });
    }
    
    public void insertNote(Note note, RepositoryCallback<Long> callback) {
        executor.execute(() -> {
            long id = database.noteDao().insert(note);
            mainHandler.post(() -> callback.onResult(id));
        });
    }
    
    public void updateNote(Note note, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.noteDao().update(note);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    public void deleteNote(Note note, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            database.noteDao().delete(note);
            mainHandler.post(() -> callback.onResult(null));
        });
    }
    
    public interface RepositoryCallback<T> {
        void onResult(T result);
    }
}

