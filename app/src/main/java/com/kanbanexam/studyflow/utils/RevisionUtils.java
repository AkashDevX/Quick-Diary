package com.kanbanexam.studyflow.utils;

import com.kanbanexam.studyflow.data.Chapter;

public class RevisionUtils {
    // Spaced repetition intervals in days: 1, 3, 7, 14, 30
    private static final long[] INTERVALS_MS = {
        1 * 24 * 60 * 60 * 1000L,  // 1 day
        3 * 24 * 60 * 60 * 1000L,  // 3 days
        7 * 24 * 60 * 60 * 1000L,  // 7 days
        14 * 24 * 60 * 60 * 1000L, // 14 days
        30 * 24 * 60 * 60 * 1000L  // 30 days
    };
    
    public static long getNextRevisionDate(int revisionLevel) {
        if (revisionLevel < 0) revisionLevel = 0;
        if (revisionLevel >= INTERVALS_MS.length) {
            return System.currentTimeMillis() + INTERVALS_MS[INTERVALS_MS.length - 1];
        }
        return System.currentTimeMillis() + INTERVALS_MS[revisionLevel];
    }
    
    public static void scheduleNextRevision(Chapter chapter) {
        chapter.lastStudied = System.currentTimeMillis();
        if (chapter.revisionLevel < INTERVALS_MS.length) {
            chapter.nextRevisionDue = getNextRevisionDate(chapter.revisionLevel);
        } else {
            chapter.nextRevisionDue = getNextRevisionDate(INTERVALS_MS.length - 1);
        }
    }
    
    public static void markRevised(Chapter chapter) {
        if (chapter.revisionLevel < 5) {
            chapter.revisionLevel++;
        }
        scheduleNextRevision(chapter);
    }
    
    public static void snoozeRevision(Chapter chapter) {
        chapter.nextRevisionDue = System.currentTimeMillis() + (24 * 60 * 60 * 1000L); // +1 day
    }
}

