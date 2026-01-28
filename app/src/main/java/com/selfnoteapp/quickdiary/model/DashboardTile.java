package com.selfnoteapp.quickdiary.model;

public class DashboardTile {
    public String title;
    public String subtitle;
    public int iconRes;
    public int gradientDrawableRes;
    public Runnable action;
    
    public DashboardTile(String title, String subtitle, int iconRes, int gradientDrawableRes, Runnable action) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconRes = iconRes;
        this.gradientDrawableRes = gradientDrawableRes;
        this.action = action;
    }
}

