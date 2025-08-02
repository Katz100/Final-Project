package com.example.final_project.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_watch_list")
public class UserWatchList {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private boolean completed = false;
    private double rating = 0.0;

    public UserWatchList(boolean completed, double rating) {
        this.completed = completed;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
