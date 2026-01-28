package com.selfnoteapp.quickdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.utils.PromptGenerator;
import com.selfnoteapp.quickdiary.utils.UiUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.List;

public class PromptsActivity extends AppCompatActivity {
    private ChipGroup categoriesContainer;
    private MaterialCardView promptCard;
    private android.widget.TextView promptText;
    private MaterialButton generateButton, usePromptButton, favoriteButton;
    private String currentPrompt = "";
    private String currentCategory = "Gratitude";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompts);
        
        try {
            categoriesContainer = findViewById(R.id.categoriesContainer);
            promptCard = findViewById(R.id.promptCard);
            if (promptCard != null) {
                promptText = promptCard.findViewById(R.id.promptText);
            }
            generateButton = findViewById(R.id.generateButton);
            usePromptButton = findViewById(R.id.usePromptButton);
            favoriteButton = findViewById(R.id.favoriteButton);
            
            setupCategories();
            
            if (generateButton != null) {
                generateButton.setOnClickListener(v -> generatePrompt());
            }
            if (usePromptButton != null) {
                usePromptButton.setOnClickListener(v -> usePrompt());
            }
            if (favoriteButton != null) {
                favoriteButton.setOnClickListener(v -> favoritePrompt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupCategories() {
        if (categoriesContainer == null) return;
        try {
            List<String> categories = PromptGenerator.getAllCategories();
            for (String category : categories) {
                Chip chip = new Chip(this);
                chip.setText(category);
                chip.setCheckable(true);
                if (category.equals(currentCategory)) {
                    chip.setChecked(true);
                }
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        currentCategory = category;
                        for (int i = 0; i < categoriesContainer.getChildCount(); i++) {
                            Chip c = (Chip) categoriesContainer.getChildAt(i);
                            if (c != chip) c.setChecked(false);
                        }
                    }
                });
                categoriesContainer.addView(chip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void generatePrompt() {
        currentPrompt = PromptGenerator.getRandomPrompt(currentCategory);
        promptText.setText(currentPrompt);
    }
    
    private void usePrompt() {
        if (currentPrompt.isEmpty()) {
            generatePrompt();
        }
        Intent intent = new Intent(this, WriteEntryActivity.class);
        intent.putExtra("prompt", currentPrompt);
        startActivity(intent);
    }
    
    private void favoritePrompt() {
        if (currentPrompt.isEmpty()) {
            Toast.makeText(this, "Generate a prompt first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            String favoritesJson = UiUtils.getFavoritePrompts(this);
            JSONArray favorites = new JSONArray(favoritesJson);
            favorites.put(currentPrompt);
            UiUtils.setFavoritePrompts(this, favorites.toString());
            Toast.makeText(this, R.string.prompt_added_to_favorites, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

