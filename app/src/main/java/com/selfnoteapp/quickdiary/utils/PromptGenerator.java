package com.selfnoteapp.quickdiary.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PromptGenerator {
    private static final List<String> GRATITUDE_PROMPTS = Arrays.asList(
        "What are three things you're grateful for today?",
        "Who made you smile today and why?",
        "What small moment brought you joy?",
        "What are you thankful for that you often take for granted?",
        "What positive change happened today?",
        "Who helped you today and how?",
        "What made today better than yesterday?",
        "What are you grateful for in your life right now?",
        "What experience today are you thankful for?",
        "What person are you most grateful to have in your life?"
    );
    
    private static final List<String> GOALS_PROMPTS = Arrays.asList(
        "What did you do today to move closer to your goals?",
        "What's one goal you want to achieve this week?",
        "What's holding you back from your biggest goal?",
        "What small step can you take tomorrow?",
        "What goal did you achieve recently?",
        "What's your most important goal right now?",
        "What would success look like for you?",
        "What resources do you need to reach your goals?",
        "What's one thing you'll do differently tomorrow?",
        "What goal are you most excited about?"
    );
    
    private static final List<String> LESSONS_PROMPTS = Arrays.asList(
        "What did you learn today?",
        "What mistake taught you something valuable?",
        "What would you tell your past self?",
        "What lesson are you still learning?",
        "What did you realize about yourself today?",
        "What advice would you give someone in your situation?",
        "What pattern do you notice in your life?",
        "What did you unlearn today?",
        "What wisdom did you gain this week?",
        "What would you do differently if you could?"
    );
    
    private static final List<String> STRESS_PROMPTS = Arrays.asList(
        "What's weighing on your mind right now?",
        "What stress can you let go of?",
        "What's causing you anxiety and why?",
        "What would make you feel more at peace?",
        "What burden are you carrying that you don't need to?",
        "What's one thing stressing you that you can't control?",
        "What helps you feel calm?",
        "What stress do you need to address?",
        "What's overwhelming you right now?",
        "How can you reduce your stress tomorrow?"
    );
    
    private static final List<String> WINS_PROMPTS = Arrays.asList(
        "What did you accomplish today?",
        "What are you proud of this week?",
        "What challenge did you overcome?",
        "What small win did you have today?",
        "What progress did you make?",
        "What are you celebrating?",
        "What did you do well today?",
        "What achievement are you most proud of?",
        "What did you complete that you've been putting off?",
        "What made you feel accomplished?"
    );
    
    private static final List<String> FUTURE_PROMPTS = Arrays.asList(
        "Where do you see yourself in one year?",
        "What do you want to remember about this time?",
        "What are you looking forward to?",
        "What do you hope changes in the next month?",
        "What future version of yourself are you becoming?",
        "What are you planning for the future?",
        "What dream are you working towards?",
        "What do you want your future self to know?",
        "What are you excited about in the coming weeks?",
        "What would make your future self proud?"
    );
    
    private static final Random random = new Random();
    
    public static List<String> getPromptsByCategory(String category) {
        switch (category) {
            case "Gratitude": return GRATITUDE_PROMPTS;
            case "Goals": return GOALS_PROMPTS;
            case "Lessons": return LESSONS_PROMPTS;
            case "Stress Dump": return STRESS_PROMPTS;
            case "Wins": return WINS_PROMPTS;
            case "Future Plan": return FUTURE_PROMPTS;
            default: return GRATITUDE_PROMPTS;
        }
    }
    
    public static String getRandomPrompt(String category) {
        List<String> prompts = getPromptsByCategory(category);
        return prompts.get(random.nextInt(prompts.size()));
    }
    
    public static String getRandomPrompt() {
        List<List<String>> allPrompts = Arrays.asList(
            GRATITUDE_PROMPTS, GOALS_PROMPTS, LESSONS_PROMPTS,
            STRESS_PROMPTS, WINS_PROMPTS, FUTURE_PROMPTS
        );
        List<String> selected = allPrompts.get(random.nextInt(allPrompts.size()));
        return selected.get(random.nextInt(selected.size()));
    }
    
    public static List<String> getAllCategories() {
        return Arrays.asList("Gratitude", "Goals", "Lessons", "Stress Dump", "Wins", "Future Plan");
    }
}

