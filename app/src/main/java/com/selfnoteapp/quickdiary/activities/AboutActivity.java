package com.selfnoteapp.quickdiary.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.selfnoteapp.quickdiary.R;

public class AboutActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

