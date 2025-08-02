package com.example.final_project.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_watch_list")
public class UserWatchList {

    @PrimaryKey(autoGenerate = true)
    public int id; // Error with no PrimaryKey annotation
    private boolean completed = false; // Default value for completed status
    private double rating = 0.0; // Default value for rating

    public UserWatchList(boolean completed, double rating) {
        this.completed = completed;
        this.rating = rating;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
