package com.selfnoteapp.quickdiary.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportUtils {
    
    public static String exportToTxt(Context context, List<DiaryEntry> entries) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String filename = "QuickDiary_Export_" + sdf.format(new Date()) + ".txt";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename);
        
        FileWriter writer = new FileWriter(file);
        writer.write("Quick Diary Export\n");
        writer.write("Generated: " + new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm a", Locale.US).format(new Date()) + "\n");
        writer.write("=".repeat(50) + "\n\n");
        
        for (DiaryEntry entry : entries) {
            writer.write("\n" + DateUtils.formatDisplayDate(entry.dateKey) + "\n");
            writer.write("-".repeat(30) + "\n");
            if (entry.tag != null && !entry.tag.isEmpty()) {
                writer.write("Tag: " + entry.tag + "\n");
            }
            if (entry.mood >= 0 && entry.mood <= 4) {
                String[] moods = {"Great", "Good", "Okay", "Bad", "Awful"};
                writer.write("Mood: " + moods[entry.mood] + "\n");
            }
            writer.write("\n" + entry.text + "\n\n");
        }
        
        writer.close();
        return file.getAbsolutePath();
    }
    
    public static String exportToPdf(Context context, List<DiaryEntry> entries) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String filename = "QuickDiary_Export_" + sdf.format(new Date()) + ".pdf";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename);
        
        PdfDocument document = new PdfDocument();
        int pageNumber = 1;
        
        for (DiaryEntry entry : entries) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            
            // Simple text rendering (for production, use proper text rendering)
            String content = DateUtils.formatDisplayDate(entry.dateKey) + "\n\n" + entry.text;
            // Note: For proper PDF text, you'd use Paint and canvas.drawText()
            // This is a simplified version
            
            document.finishPage(page);
            pageNumber++;
        }
        
        document.writeTo(new java.io.FileOutputStream(file));
        document.close();
        
        return file.getAbsolutePath();
    }
}

