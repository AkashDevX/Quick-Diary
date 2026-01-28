package com.selfnoteapp.quickdiary.repo;

import android.content.Context;
import com.selfnoteapp.quickdiary.data.AppDatabase;
import com.selfnoteapp.quickdiary.data.DiaryDao;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiaryRepository {
    private DiaryDao diaryDao;
    private ExecutorService executor;
    
    public DiaryRepository(Context context) {
        try {
            AppDatabase db = AppDatabase.getDatabase(context);
            diaryDao = db.diaryDao();
            executor = Executors.newSingleThreadExecutor();
        } catch (Exception e) {
            e.printStackTrace();
            executor = Executors.newSingleThreadExecutor();
        }
    }
    
    public void getByDate(String dateKey, RepositoryCallback<DiaryEntry> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(null);
            }
            return;
        }
        executor.execute(() -> {
            try {
                DiaryEntry entry = diaryDao.getByDate(dateKey);
                if (callback != null) {
                    callback.onComplete(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(null);
                }
            }
        });
    }
    
    public void insert(DiaryEntry entry, RepositoryCallback<Long> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(-1L);
            }
            return;
        }
        executor.execute(() -> {
            try {
                long id = diaryDao.insert(entry);
                if (callback != null) {
                    callback.onComplete(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(-1L);
                }
            }
        });
    }
    
    public void update(DiaryEntry entry, RepositoryCallback<Void> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(null);
            }
            return;
        }
        executor.execute(() -> {
            try {
                diaryDao.update(entry);
                if (callback != null) {
                    callback.onComplete(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(null);
                }
            }
        });
    }
    
    public void getAllDesc(RepositoryCallback<List<DiaryEntry>> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(new java.util.ArrayList<>());
            }
            return;
        }
        executor.execute(() -> {
            try {
                List<DiaryEntry> entries = diaryDao.getAllDesc();
                if (callback != null) {
                    callback.onComplete(entries);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(new java.util.ArrayList<>());
                }
            }
        });
    }
    
    public void search(String query, RepositoryCallback<List<DiaryEntry>> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(new java.util.ArrayList<>());
            }
            return;
        }
        executor.execute(() -> {
            try {
                List<DiaryEntry> entries = diaryDao.search(query);
                if (callback != null) {
                    callback.onComplete(entries);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(new java.util.ArrayList<>());
                }
            }
        });
    }
    
    public void getAllDateKeys(RepositoryCallback<List<String>> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(new java.util.ArrayList<>());
            }
            return;
        }
        executor.execute(() -> {
            try {
                List<String> dates = diaryDao.getAllDateKeys();
                if (callback != null) {
                    callback.onComplete(dates);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(new java.util.ArrayList<>());
                }
            }
        });
    }
    
    public void deleteAll(RepositoryCallback<Void> callback) {
        if (diaryDao == null) {
            if (callback != null) {
                callback.onComplete(null);
            }
            return;
        }
        executor.execute(() -> {
            try {
                diaryDao.deleteAll();
                if (callback != null) {
                    callback.onComplete(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onComplete(null);
                }
            }
        });
    }
    
    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }
}

